package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import in.deloitte.screening.app.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.document.entities.Resume;
import in.deloitte.screening.app.document.entities.ResumeDownloadedUserInfo;
import in.deloitte.screening.app.document.repositories.JobDescriptionRepository;
import in.deloitte.screening.app.document.repositories.ResumeRepository;
import in.deloitte.screening.app.document.utils.DocumentTextAnalyzer;
import in.deloitte.screening.app.document.utils.DocumentTextExtractor;
import in.deloitte.screening.app.document.utils.VectorizeDocumentText;
import in.deloitte.screening.app.skills.repositories.SkillsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import static in.deloitte.screening.app.document.utils.DocumentTextAnalyzer.stopWords;


@Service
public class ResumeServiceImpl implements ResumeService {
	
	private static final Logger logger = LogManager.getLogger(ResumeServiceImpl.class);
	
	@Autowired
	@Qualifier("pdf")
	private DocumentTextExtractor pdfTextExtractor;

	@Autowired
	@Qualifier("docx")
	private DocumentTextExtractor docxTextExtractor;

	@Autowired
	private DocumentTextAnalyzer documentTextAnalyzer;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ResumeRepository resumeRepository;

	@Autowired
	private VectorizeDocumentText vectorizeDocumentText;
	
	@Autowired
	JobDescriptionRepository jobDescriptionRepository;

	@Autowired
	private SkillsRepository skillsRepository;
	

	/**
	 * 
	 * @param resumes
	 * @param userEmail
	 * 
	 * @returns Returns UploadResumesResponse after saving data in Applicant and
	 *          Resume tables
	 * 
	 * @throws IOException
	 * 
	 */
	@Transactional
	@Override
	public UploadResumesResponse saveResumeInfo(List<MultipartFile> resumes, String userEmail) throws IOException {

		String cvText = null;
		UploadResumesResponse response = new UploadResumesResponse();
		List<String> allSkills = skillsRepository.getAllSkills();
		logger.info("skills available in DB : {}" + allSkills);
		
		List<Resume> newResumesList = new ArrayList<>();

		for (MultipartFile resume : resumes) {

			if (resume.getOriginalFilename().endsWith("pdf"))
				cvText = pdfTextExtractor.extractText(resume).toLowerCase();
			else if (resume.getOriginalFilename().endsWith("docx"))
				cvText = docxTextExtractor.extractText(resume).toLowerCase();

			String name = resume.getOriginalFilename().substring(0, resume.getOriginalFilename().indexOf('.'));

			String email = name + "@nomail.com";
			Optional<String> optionalEmail = getEmail(cvText);
			if (optionalEmail.isPresent()) {
				String mailSuffix = getMailSuffix(optionalEmail.get());
				email = optionalEmail.get().split("@")[0] + mailSuffix;
			}

			String experience = getExperience(cvText);
			
			String sw = DocumentTextAnalyzer.stopWords();
			Analyzer analyzer = new StopAnalyzer(new CharArraySet(Arrays.asList(sw.split(" ")), true));
			List<String> cvToken = documentTextAnalyzer.tokenizeText(analyzer, cvText);

			List<String> cvSkills = new ArrayList<>();
			for (int i = 0; i < cvToken.size() - 1; i++) {
				if (allSkills.contains(cvToken.get(i) + " " + cvToken.get(i + 1)))
					cvSkills.add(cvToken.get(i) + " " + cvToken.get(i + 1));
				else if (allSkills.contains(cvToken.get(i)))
					cvSkills.add(cvToken.get(i));
			}

			String fileName = resume.getOriginalFilename();
			LocalDateTime uploadTime = LocalDateTime.now();
			Map<String, Long> cvSkillsVector = vectorizeDocumentText.getVector(cvSkills);
			logger.debug("Resume skills vector : {}" + cvSkillsVector);

			Resume res = new Resume();
			res.setEmail(email);
			res.setExperience(Double.parseDouble(experience));
			res.setResumeFile(resume.getBytes());
			res.setResumeFileName(fileName.substring(0, fileName.indexOf(".")));
			res.setDocType(fileName.substring(fileName.indexOf(".") + 1));
			res.setText(cvText);
			res.setSkills(cvSkillsVector.keySet());
			res.setUploadedBy(userEmail);
			res.setUploadTime(uploadTime);
			res.setVector(cvSkillsVector);
			newResumesList.add(res);

		}

		resumeRepository.saveAll(newResumesList);
		response.setMessage(newResumesList.size() + " resumes uploaded successfully...");
		return response;
	}

	/**
	 * 
	 * @param email
	 * 
	 * @return Returns email suffix from cvText
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
	 * @param cvText
	 * 
	 * @return Returns experience from cvText
	 * 
	 */
	public String getExperience(String cvText) {

		String experience = "";
		int expIndex = cvText.indexOf("years");
		
		if (expIndex > 8) {
			for (int i = expIndex - 2; i >= expIndex - 7; i--) {

				if (cvText.charAt(i) == ' ' || cvText.charAt(i) == '.' || cvText.charAt(i) == '@')
					break;
				if(cvText.charAt(i) != '+')
					experience = cvText.charAt(i) + experience;
			}
		} 
		else {
			experience = "1";
		}

		return experience;
	}

	/**
	 * 
	 * @param cvText
	 * 
	 * @return Returns Mobile number from cvText
	 * 
	 */
	public String getMobileNumber(String cvText) {

		Pattern pattern = Pattern.compile("\\b\\d{10,12}\\b");
		Matcher matcher = pattern.matcher(cvText);
		String mobile = "";

		while (matcher.find()) {
			mobile = matcher.group();
			break;
		}

		return mobile.length() == 10 ? mobile : mobile.substring(2);
	}

	/**
	 * 
	 * @param cvText
	 * 
	 * @return Returns email from cvText
	 * 
	 */
	public Optional<String> getEmail(String cvText) {
		return Arrays.stream(cvText.split(" ")).filter(s -> checkMailSuffix(s)).findFirst();
	}

	public boolean checkMailSuffix(String s) {

		return s.contains("@gmail.com") ? true
				: (s.contains("@yahoo.com") ? true
						: (s.contains("@outlook.com") ? true
								: (s.contains("@ymail.com") ? true
										: (s.contains("@deloitte.com") ? true
												: (s.contains("hotmail.co.in") ? true : false)))));
	}
	

	@Transactional
	@Override
	public String saveDownloadedUserInfo(ResumeDownloadedUserRequest request) {
		
		ResumeDownloadedUserInfo resumeDownloadedUserInfo = new ResumeDownloadedUserInfo();
		resumeDownloadedUserInfo.setApplicantEmail(request.getApplicantEmail());
		resumeDownloadedUserInfo.setDownloadedUserEmail(request.getUserEmail());
		resumeDownloadedUserInfo.setDownloadTime(LocalDateTime.now());
		
		int resumeId = resumeRepository.getResumeData(request.getApplicantEmail()).getId();
		
		resumeRepository.saveDownloadedUserInfo(resumeId, request.getApplicantEmail(),
				LocalDateTime.now(), request.getUserEmail());
		
		return "Updated...";
	}
}
