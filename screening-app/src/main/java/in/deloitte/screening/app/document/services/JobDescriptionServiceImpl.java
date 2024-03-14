package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.document.dto.SearchJdResponse;
import in.deloitte.screening.app.document.dto.UploadJdResponse;
import in.deloitte.screening.app.document.entities.JobDescription;
import in.deloitte.screening.app.document.repositories.DocumentQueries;
import in.deloitte.screening.app.document.repositories.JobDescriptionRepository;
import in.deloitte.screening.app.document.utils.DocumentContentAnalyzer;
import in.deloitte.screening.app.document.utils.DocumentContentExtractionService;
import in.deloitte.screening.app.document.utils.VectorizeDocumentContent;
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
	DocumentContentExtractionService pdfContentExtractor;
	
	@Autowired
	@Qualifier("docx")
	DocumentContentExtractionService docxContentExtractor;
	
	@Autowired
	DocumentContentAnalyzer documentContentAnalyzer;
	
	@Autowired
	SkillsRepository skillsRepository;
	
	@Autowired
	VectorizeDocumentContent vectorizeJdContent;
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public UploadJdResponse saveJobDescription(MultipartFile jd, String userEmail) throws IOException {
		
		String jdFileName = jd.getOriginalFilename();
		String jdText = "";
		
		if(jdFileName.endsWith("pdf"))
			jdText = pdfContentExtractor.extractContent(jd).toLowerCase();
		else if(jdFileName.endsWith("docx"))
			jdText = docxContentExtractor.extractContent(jd).toLowerCase();
		
		String sw = documentContentAnalyzer.stopWords();
		Analyzer analyzer = new StopAnalyzer(new CharArraySet(Arrays.asList(sw.split(" ")), true));
		List<String> jdToken = documentContentAnalyzer.tokenizeContent(analyzer, jdText);
		
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

		JobDescription jobDescription = new JobDescription(null, jdFileName.substring(0, jdFileName.indexOf(".")),
				jd.getBytes(), jdText, jdVector, userEmail, LocalDateTime.now());
		
		jobDescriptionRepository.save(jobDescription);
		String message = "Job Description is uploaded";
		return new UploadJdResponse(message, jdFileName.substring(0, jdFileName.indexOf(".")));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SearchJdResponse> getAllJdInfo(String jdFileName) {
			
		return entityManager
				.createNativeQuery(DocumentQueries.createJdResultsQuery(jdFileName), SearchJdResponse.class)
				.getResultList();
	}
}
