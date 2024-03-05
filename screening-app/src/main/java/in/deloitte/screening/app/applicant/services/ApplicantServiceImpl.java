package in.deloitte.screening.app.applicant.services;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.math3.fraction.Fraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.applicant.dto.SearchProfilesRequest;
import in.deloitte.screening.app.applicant.dto.SearchProfilesResponse;
import in.deloitte.screening.app.applicant.entities.Applicant;
import in.deloitte.screening.app.applicant.entities.ApplicantResume;
import in.deloitte.screening.app.applicant.repositories.ApplicantQueries;
import in.deloitte.screening.app.applicant.repositories.ApplicantRepository;
import in.deloitte.screening.app.exceptions.DataNotFoundException;
import in.deloitte.screening.app.resume.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.resume.document.entities.Resume;
import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUserInfo;
import in.deloitte.screening.app.resume.document.repositories.ResumeRepository;
import in.deloitte.screening.app.resume.document.services.CosineSimilarityCalculationService;
import in.deloitte.screening.app.resume.document.services.DocumentContentAnalyzer;
import in.deloitte.screening.app.resume.document.services.DocumentContentExtractionService;
import in.deloitte.screening.app.resume.document.services.VectorizeDocumentContent;
import in.deloitte.screening.app.skills.repositories.SkillsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class ApplicantServiceImpl implements ApplicantService {

	private static final Logger logger = LogManager.getLogger(ApplicantServiceImpl.class);

	@Autowired
	@Qualifier("pdf")
	DocumentContentExtractionService pdfContentExtractor;

	@Autowired
	@Qualifier("docx")
	DocumentContentExtractionService docxContentExtractor;

	@Autowired
	ApplicantRepository applicantRepository;

	@Autowired
	DocumentContentAnalyzer documentContentAnalyzer;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	ResumeRepository resumeRepository;

	@Autowired
	VectorizeDocumentContent vectorizeDocumentContent;

	@Autowired
	CosineSimilarityCalculationService cosineSimilarityCalculationService;

	@Autowired
	SkillsRepository skillsRepository;

	/**
	 * 
	 * @param resumes
	 * @param userEmail
	 * 
	 * @Returns UploadResumesResponse after saving data in Applicant and Resume tables
	 * 
	 * @throws IOException
	 * 
	 */
	@Transactional
	@Override
	public UploadResumesResponse saveApplicantInfo(List<MultipartFile> resumes, String userEmail) throws IOException {

		String cvContent = null;
		UploadResumesResponse response = new UploadResumesResponse();
		List<String> allSkills = skillsRepository.getAllSkills();
		logger.info("skills available in DB : {}" + allSkills);

		List<Applicant> newApplicantsList = new ArrayList<>();
		List<Resume> newResumesList = new ArrayList<>();

		for (MultipartFile resume : resumes) {

			if (resume.getOriginalFilename().endsWith("pdf"))
				cvContent = pdfContentExtractor.extractContent(resume).toLowerCase();
			else if (resume.getOriginalFilename().endsWith("docx"))
				cvContent = docxContentExtractor.extractContent(resume).toLowerCase();

			String name = resume.getOriginalFilename().substring(0, resume.getOriginalFilename().indexOf('.'));
			
			String email = name + "@nomail.com";
			Optional<String> optionalEmail = getEmail(cvContent);
			if(optionalEmail.isPresent()) {
				String mailSuffix = getMailSuffix(optionalEmail.get());
				email = optionalEmail.get().split("@")[0] + mailSuffix;
			}
			
			String experience = getExperience(cvContent);
			
			String jobTitle = getJobTitle(Double.parseDouble(experience));

			List<String> prefferedLocations = getPrefferedLocations(cvContent);
			prefferedLocations.add("Pan India");
			prefferedLocations.add("Remote");

			String sw = stopWords();
			Analyzer analyzer = new StopAnalyzer(new CharArraySet(Arrays.asList(sw.split(" ")), true));
			List<String> cvToken = documentContentAnalyzer.tokenizeContent(analyzer, cvContent);

			List<String> cvSkills = new ArrayList<>();
			for (int i = 0; i < cvToken.size() - 1; i++) {
				if (allSkills.contains(cvToken.get(i) + " " + cvToken.get(i + 1)))
					cvSkills.add(cvToken.get(i) + " " + cvToken.get(i + 1));
				else if (allSkills.contains(cvToken.get(i)))
					cvSkills.add(cvToken.get(i));
			}

			String fileName = resume.getOriginalFilename();
			LocalDateTime uploadTime = LocalDateTime.now();
			Map<String, Long> cVSkillsVector = vectorizeDocumentContent.getVector(cvSkills);
			logger.debug("Resume skills vector : {}" + cVSkillsVector);

			Resume res = new Resume();
			res.setEmail(email);
			res.setResumeFile(resume.getBytes());
			res.setDocType(fileName.substring(fileName.indexOf(".") + 1));
			res.setText(cvContent);
			res.setUploadedBy(userEmail);
			res.setUploadTime(uploadTime);
			res.setVector(cVSkillsVector);
			newResumesList.add(res);


			Applicant newApplicant = new Applicant(email, name, experience, jobTitle, prefferedLocations, userEmail,
					uploadTime, cVSkillsVector.keySet());

			newApplicantsList.add(newApplicant);
		}

		applicantRepository.saveAll(newApplicantsList);
		resumeRepository.saveAll(newResumesList);
		response.setMessage(newApplicantsList.size() + " resumes uploaded successfully...");
		return response;
	}

	/**
	 * 
	 * @param email
	 * 
	 * @return Returns email suffix from cvContent
	 * 
	 */
	private String getMailSuffix(String email) {
		return email
				.contains("@gmail.com")
						? "@gmail.com"
						: (email.contains(
								"@yahoo.com")
										? "@yahoo.com"
										: (email.contains(
												"@outlook.com")
														? "@outlook.com"
														: (email.contains("@ymail.com") ? "@ymail.com"
																: (email.contains("@deloitte.com") ? "@deloitte.com"
																		: (email.contains("hotmail.co.in")
																				? "hotmail.co.in"
																				: "@nomail.com")))));
	}

	/**
	 * 
	 * @param cvContent
	 * 
	 * @return Return experience from cvContent
	 * 
	 */
	public String getExperience(String cvContent) {

		String experience = "";
		int expIndex = cvContent.indexOf("years");

		for (int i = expIndex - 2; i >= expIndex - 7; i--) {
			if (cvContent.charAt(i) == ' ')
				break;
			experience = cvContent.charAt(i) + experience;
		}

		return experience;
	}

	/**
	 * 
	 * @param cvContent
	 * 
	 * @return Return Mobile number from cvContent
	 * 
	 */
	public String getMobileNumber(String cvContent) {

		Pattern pattern = Pattern.compile("\\b\\d{10,12}\\b");
		Matcher matcher = pattern.matcher(cvContent);
		String mobile = "";

		while (matcher.find()) {
			mobile = matcher.group();
			System.out.println("mobile : " + mobile);
			break;
		}

		return mobile.length() == 10 ? mobile : mobile.substring(2);
	}

	/**
	 * 
	 * @param cvContent
	 * 
	 * @return Returns email
	 * 
	 */
	public Optional<String> getEmail(String cvContent) {
		return Arrays.stream(cvContent.split(" ")).filter(s -> checkMailSuffix(s)).findFirst();
	}

	public boolean checkMailSuffix(String s) {

		return s.contains("@gmail.com") ? true
				: (s.contains("@yahoo.com") ? true
						: (s.contains("@outlook.com") ? true
								: (s.contains("@ymail.com") ? true
										: (s.contains("@deloitte.com") ? true
												: (s.contains("hotmail.co.in") ? true : false)))));
	}

	/**
	 * 
	 * @param experience
	 * 
	 * @return Returns jodTitle
	 * 
	 */
	public String getJobTitle(double experience) {

		return (experience >= 1 && experience < 3) ? "Analyst"
				: ((experience >= 3 && experience < 6) ? "Consultant"
						: ((experience >= 6 && experience < 10) ? "Senior Consultant"
								: ((experience >= 10 && experience < 15) ? "Manager" : "Associate Director")));
	}

	/**
	 * 
	 * @param cvContent
	 * 
	 * @return Returns preferred locations of applicant
	 * 
	 */
	public List<String> getPrefferedLocations(String cvContent) {

		List<String> locationsList = Arrays.asList("Hyderabad", "Banglore", "Pune", "Thane", "chennai", "Coimbatore",
				"Mumbai", "Kolkata", "Goa", "Delhi", "Noida", "Kochi", "Gurugram", "Noida");

		return locationsList.stream().filter(location -> cvContent.contains(location.toLowerCase())).toList();
	}

	/**
	 * 
	 * @param searchProfilesRequest, jobDescriptionFile
	 * 
	 * @return Returns list of matched profiles
	 * 
	 */
	@Override
	public List<SearchProfilesResponse> matchingProfilesResponse(SearchProfilesRequest request,
			MultipartFile jobDescriptionFile) throws IOException {

		logger.info(
				"Started executing matchingProfilesResponse(SearchProfilesRequest request, MultipartFile jobDescriptionFile)...");

		@SuppressWarnings("unchecked")
		List<Applicant> applicantList = entityManager
				.createNativeQuery(ApplicantQueries.createApplicantResultsQuery(request), Applicant.class)
				.getResultList();

		List<Resume> resumesList = resumeRepository.getAllResumeData();

		List<ApplicantResume> applicantResumesList = new ArrayList<>();
		for (int i = 0; i < resumesList.size(); i++) {
			for (int j = 0; j < applicantList.size(); j++) {
				if (resumesList.get(i).getEmail().equals(applicantList.get(j).getEmail())) {
					ApplicantResume ar = new ApplicantResume();
					ar.setApplicant(applicantList.get(j));
					ar.setResume(resumesList.get(i));
					applicantResumesList.add(ar);
				}
			}
		}
		logger.debug("Matched applicants, resumes list : {}", applicantResumesList);

		if (applicantList == null || applicantList.size() == 0) {
			logger.error("No results found list is null or empty, resultList: {}", applicantList);
			throw new DataNotFoundException("No data found for given input...");
		}

		logger.info("{} Profiles available for {} Role", applicantList.size(), request.getJobTitle());
		logger.debug("Available profiles data : {}", applicantList);

		String jdContent = pdfContentExtractor.extractContent(jobDescriptionFile);
		logger.debug("Cleaned JD content : {}", jdContent);

		Analyzer analyzer = new StopAnalyzer(new CharArraySet(Arrays.asList(stopWords().split(" ")), true));
		List<String> jdToken = documentContentAnalyzer.tokenizeContent(analyzer, jdContent.toLowerCase());
		logger.debug("JD content tokens : {}", jdToken);

		List<String> jdSkills = new ArrayList<>();
		List<String> skills = skillsRepository.getAllSkills();
		for (String skill : skills) {
			if (jdToken.contains(skill)) {
				jdSkills.add(skill);
			}
		}
		for (int i = 0; i < jdToken.size() - 1; i++) {
			if (skills.contains(jdToken.get(i) + " " + jdToken.get(i + 1)))
				jdSkills.add(jdToken.get(i) + " " + jdToken.get(i + 1));
		}
		logger.debug("skills in JD : {}", jdSkills);

		Map<String, Long> jdVector = vectorizeDocumentContent.getVector(jdSkills);
		logger.debug("JD skills vector : {}", jdVector);

		List<SearchProfilesResponse> responses = applicantResumesList.stream()
				.map(applicant -> setSearchProfilesResponse(applicant, jdVector))
				.sorted((o1, o2) -> o1.getProfileMatch().compareTo(o2.getProfileMatch())).collect(Collectors.toList());
		Collections.reverse(responses);
		return responses;
	}

	/**
	 * 
	 * @param applicant
	 * @param jdVector
	 * 
	 * @return Returns a matched profile
	 * 
	 * @throws IOException
	 * 
	 */
	public SearchProfilesResponse setSearchProfilesResponse(ApplicantResume applicantResume,
			Map<String, Long> jdVector) {

		logger.info("Started preparing response of applicant : {}", applicantResume.getApplicant().getEmail());

		Resume resume = applicantResume.getResume();
		logger.info("{} Resume data : {}", applicantResume.getApplicant().getEmail(), resume);

		List<ResumeDownloadedUserInfo> resumeDownloadsInfo = resume.getDownloads();
		logger.info("Resume downloaded users for apllicant {} : {}", resume.getEmail(), resumeDownloadsInfo);

		Map<String, Long> cvVector = resume.getVector();
		logger.info("Resume skills vector from DB : {}", cvVector);

		double cosineSimilarity = 0.0;
		try {
			cosineSimilarity = cosineSimilarityCalculationService.getCosineSimilarity(cvVector, jdVector);
		} catch (IOException e) {
			logger.error("Error occured while calculating cosine similarity");
			e.printStackTrace();
		}
		logger.debug("Cosine similarity value : {}", cosineSimilarity);

		Fraction fraction = new Fraction(cosineSimilarity * 100);
		DecimalFormat df = new DecimalFormat("#.##");
		Double profileMatch = Double.parseDouble(df.format(fraction.doubleValue()));
		logger.info("profile match with JD by {}", profileMatch);

		Applicant applicant = applicantResume.getApplicant();
		return new SearchProfilesResponse(applicant.getEmail(), profileMatch, applicant.getSkills(),
				applicant.getExperience() + " years", applicant.getUploadedBy(), applicant.getUploadTime(),
				resume.getResumeFile(), resumeDownloadsInfo);
	}

	/**
	 * 
	 * @return Returns stop words to be removed
	 * 
	 */
	public String stopWords() {
		return "0o 0s 3a 3b 3d 6b 6o a a1 a2 a3 a4 ab able about above abst ac accordance according accordingly across \r\n"
				+ "act actually ad added adj ae af affected affecting affects after afterwards ag again against ah ain ain't aj \r\n"
				+ "al all allow allows almost alone along already also although always am among amongst amoungst \r\n"
				+ "amount an and announce another any anybody anyhow anymore anyone anything anyway anyways \r\n"
				+ "anywhere ao ap apart apparently appear appreciate appropriate approximately ar are aren arent aren't \r\n"
				+ "arise around as a's aside ask asking associated at au auth av available aw away awfully ax ay az b b1 b2 \r\n"
				+ "b3 ba back bc bd be became because become becomes becoming been before beforehand begin \r\n"
				+ "beginning beginnings begins behind being believe below beside besides best better between beyond bi \r\n"
				+ "bill biol bj bk bl bn both bottom bp br brief briefly bs bt bu but bx by c c1 c2 c3 ca call came can cannot \r\n"
				+ "cant can't cause causes cc cd ce certain certainly cf cg ch changes ci cit cj cl clearly cm c'mon cn co com \r\n"
				+ "come comes con concerning consequently consider considering contain containing contains \r\n"
				+ "corresponding could couldn couldnt couldn't course cp cq cr cry cs c's ct cu currently cv cx cy cz d d2 da \r\n"
				+ "date dc dd de definitely describe described despite detail df di did didn didn't different dj dk dl do does \r\n"
				+ "doesn doesn't doing don done don't down downwards dp dr ds dt du due during dx dy e e2 e3 ea each \r\n"
				+ "ec ed edu ee ef effect eg ei eight eighty either ej el eleven else elsewhere em empty en end ending \r\n"
				+ "enough entirely eo ep eq er es especially est et et-al etc eu ev even ever every everybody everyone \r\n"
				+ "everything everywhere ex exactly example except ey f f2 fa far fc few ff fi fifteen fifth fify fill find fire first \r\n"
				+ "five fix fj fl fn fo followed following follows for former formerly forth forty found four fr from front fs ft \r\n"
				+ "fu full further furthermore fy g ga gave ge get gets getting gi give given gives giving gj gl go goes going \r\n"
				+ "gone got gotten gr greetings gs gy h h2 h3 had hadn hadn't happens hardly has hasn hasnt hasn't have \r\n"
				+ "haven haven't having he hed he'd he'll hello help hence her here hereafter hereby herein heres here's \r\n"
				+ "hereupon hers herself hes he's hh hi hid him himself his hither hj ho home hopefully how howbeit \r\n"
				+ "however how's hr hs http hu hundred hy i i2 i3 i4 i6 i7 i8 ia ib ibid ic id i'd ie if ig ignored ih ii ij il i'll im i'm \r\n"
				+ "immediate immediately importance important in inasmuch inc indeed index indicate indicated indicates \r\n"
				+ "information inner insofar instead interest into invention inward io ip iq ir is isn isn't it itd it'd it'll its it's \r\n"
				+ "itself iv i've ix iy iz j jj jr js jt ju just k ke keep keeps kept kg kj km know known knows ko l l2 la largely last \r\n"
				+ "lately later latter latterly lb lc le least les less lest let lets let's lf like liked likely line little lj ll ll ln lo look\r\n"
				+ "looking looks los lr ls lt ltd m m2 ma made mainly make makes many may maybe me mean means\r\n"
				+ "meantime meanwhile merely mg might mightn mightn't mill million mine miss ml mn mo more \r\n"
				+ "moreover most mostly move mr mrs ms mt mu much mug must mustn mustn't my myself n n2 na name \r\n"
				+ "namely nay nc nd ne near nearly necessarily necessary need needn needn't needs neither never \r\n"
				+ "nevertheless new next ng ni nine ninety nj nl nn no nobody non none nonetheless noone nor normally \r\n"
				+ "nos not noted nothing novel now nowhere nr ns nt ny o oa ob obtain obtained obviously oc od of off \r\n"
				+ "often og oh oi oj ok okay ol old om omitted on once one ones only onto oo op oq or ord os ot other \r\n"
				+ "others otherwise ou ought our ours ourselves out outside over overall ow owing own ox oz p p1 p2 p3 \r\n"
				+ "page pagecount pages par part particular particularly pas past pc pd pe per perhaps pf ph pi pj pk pl \r\n"
				+ "placed please plus pm pn po poorly possible possibly potentially pp pq pr predominantly present \r\n"
				+ "presumably previously primarily probably promptly proud provides ps pt pu put py q qj qu que quickly \r\n"
				+ "quite qv r r2 ra ran rather rc rd re readily really reasonably recent recently ref refs regarding regardless \r\n"
				+ "regards related relatively research research-articl respectively resulted resulting results rf rh ri right rj rl \r\n"
				+ "rm rn ro rq rr rs rt ru run rv ry s s2 sa said same saw say saying says sc sd se sec second secondly section \r\n"
				+ "see seeing seem seemed seeming seems seen self selves sensible sent serious seriously seven several sf \r\n"
				+ "shall shan shan't she shed she'd she'll shes she's should shouldn shouldn't should've show showed \r\n"
				+ "shown showns shows si side significant significantly similar similarly since sincere six sixty sj sl slightly sm \r\n"
				+ "sn so some somebody somehow someone somethan something sometime sometimes somewhat \r\n"
				+ "somewhere soon sorry sp specifically specified specify specifying sq sr ss st still stop strongly sub \r\n"
				+ "substantially successfully such sufficiently suggest sup sure sy system sz t t1 t2 t3 take taken taking tb tc \r\n"
				+ "td te tell ten tends tf th than thank thanks thanx that that'll thats that's that've the their theirs them \r\n"
				+ "themselves then thence there thereafter thereby thered therefore therein there'll thereof therere \r\n"
				+ "theres there's thereto thereupon there've these they theyd they'd they'll theyre they're they've thickv \r\n"
				+ "thin think third this thorough thoroughly those thou though thoughh thousand three throug through \r\n"
				+ "throughout thru thus ti til tip tj tl tm tn to together too took top toward towards tp tq tr tried tries truly \r\n"
				+ "try trying ts t's tt tv twelve twenty twice two tx u u201d ue ui uj uk um un under unfortunately unless \r\n"
				+ "unlike unlikely until unto uo up upon ups ur us use used useful usefully usefulness uses using usually ut v \r\n"
				+ "va value various vd ve ve very via viz vj vo vol vols volumtype vq vs vt vu w wa want wants was wasn \r\n"
				+ "wasnt wasn't way we wed we'd welcome well we'll well-b went were we're weren werent weren't we've \r\n"
				+ "what whatever what'll whats what's when whence whenever when's where whereafter whereas \r\n"
				+ "whereby wherein wheres where's whereupon wherever whether which while whim whither who whod \r\n"
				+ "whoever whole who'll whom whomever whos who's whose why why's wi widely will willing wish with \r\n"
				+ "within without wo won wonder wont won't words world would wouldn wouldnt wouldn't www x x1 x2 \r\n"
				+ "x3 xf xi xj xk xl xn xo xs xt xv xx y y2 yes yet yj yl you youd you'd you'll your youre you're yours yourself \r\n"
				+ "yourselves you've yr ys yt z zero zi zz and for in to a in an of ";
	}

}
