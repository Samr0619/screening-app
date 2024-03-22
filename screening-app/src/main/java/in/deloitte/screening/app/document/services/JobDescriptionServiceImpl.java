package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.document.dto.SearchJdResponse;
import in.deloitte.screening.app.document.dto.UploadJdResponse;
import in.deloitte.screening.app.document.entities.JobDescription;
import in.deloitte.screening.app.document.entities.Resume;
import in.deloitte.screening.app.document.entities.ResumeJobDescription;
import in.deloitte.screening.app.document.repositories.DocumentQueries;
import in.deloitte.screening.app.document.repositories.JobDescriptionRepository;
import in.deloitte.screening.app.document.repositories.ResumeJobDescriptionRespository;
import in.deloitte.screening.app.document.repositories.ResumeRepository;
import in.deloitte.screening.app.document.utils.DocumentTextAnalyzer;
import in.deloitte.screening.app.document.utils.DocumentTextExtractor;
import in.deloitte.screening.app.document.utils.VectorizeDocumentText;
import in.deloitte.screening.app.skills.repositories.SkillsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class JobDescriptionServiceImpl implements JobDescriptionService {
	
	private static final Logger logger = LogManager.getLogger(JobDescriptionServiceImpl.class);
	
	@Autowired
	JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	@Qualifier("pdf")
	DocumentTextExtractor pdfContentExtractor;
	
	@Autowired
	@Qualifier("docx")
	DocumentTextExtractor docxContentExtractor;
	
	@Autowired
	DocumentTextAnalyzer documentContentAnalyzer;
	
	@Autowired
	SkillsRepository skillsRepository;
	
	@Autowired
	VectorizeDocumentText vectorizeJdContent;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	ResumeRepository resumeRepository;
	
	@Autowired
	private CosineSimilarityCalculationService cosineSimilarityCalculationService;
	
	@Autowired
	ResumeJobDescriptionRespository resumeJobDescriptionRespository;

//	@Transactional
	@Override
	public UploadJdResponse saveJobDescription(MultipartFile jd, String userEmail) throws IOException {
		
		String jdOriginalFileName = jd.getOriginalFilename();
		String jdText = "";
		
		if(jdOriginalFileName.endsWith("pdf"))
			jdText = pdfContentExtractor.extractText(jd).toLowerCase();
		else if(jdOriginalFileName.endsWith("docx"))
			jdText = docxContentExtractor.extractText(jd).toLowerCase();
		
		String sw = documentContentAnalyzer.stopWords();
		Analyzer analyzer = new StopAnalyzer(new CharArraySet(Arrays.asList(sw.split(" ")), true));
		List<String> jdToken = documentContentAnalyzer.tokenizeText(analyzer, jdText);
		
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

		Map<String, Long> jdVector = vectorizeJdContent.getVector(jdSkills);
		logger.debug("JD skills vector : {}", jdVector);
		
		String jdFileName = jdOriginalFileName.substring(0, jdOriginalFileName.indexOf("."));
		
		List<ResumeJobDescription> resumeJdList = new ArrayList<>();
		LocalDate currentDate = LocalDate.now();
		LocalDate thirtyDaysAgoDate = LocalDate.now(ZoneId.of("Asia/Kolkata")).minusDays(30);
		List<Resume> resumes = resumeRepository.getResumesDataOfLast30Days(thirtyDaysAgoDate, currentDate);
		for(Resume res : resumes) {
			
			ResumeJobDescription resumeJobDescription = new ResumeJobDescription();
			resumeJobDescription.setResumeFileName(res.getResumeFileName());
			resumeJobDescription.setJdFileName(jdFileName);
			
			Map<String, Long> cvVector = res.getVector();
			logger.info("Resume skills vector from DB : {}", cvVector);

			double cosineSimilarity = 0.0;
			try {
				try {
					cosineSimilarity = cosineSimilarityCalculationService.getCosineSimilarity(cvVector, jdVector);
				}
				catch(MathArithmeticException ex) {
					cosineSimilarity = 0.0;
				}
			} catch (IOException e) {
				logger.error("Error occured while calculating cosine similarity.");
				e.printStackTrace();
			}
			logger.debug("Cosine similarity value : {}", cosineSimilarity);

			Fraction fraction = new Fraction(cosineSimilarity * 100);
			DecimalFormat df = new DecimalFormat("#.##");
			Double profileMatch = Double.parseDouble(df.format(fraction.doubleValue()));
			
			resumeJobDescription.setCosineSimilarity(profileMatch);
			
			resumeJdList.add(resumeJobDescription);
		}

		JobDescription jobDescription = new JobDescription(null, jdFileName, jd.getBytes(), 
				jdText, jdVector, userEmail, LocalDateTime.now());
		
		jobDescriptionRepository.save(jobDescription);
		resumeJobDescriptionRespository.saveAll(resumeJdList);
		String message = "Job Description is uploaded";
		return new UploadJdResponse(message, jdFileName,HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SearchJdResponse> getAllJdInfo(String jdFileName) {
		
		return entityManager
				.createNativeQuery(DocumentQueries.createJdResultsQuery(jdFileName), SearchJdResponse.class)
				.getResultList();
	}

}
