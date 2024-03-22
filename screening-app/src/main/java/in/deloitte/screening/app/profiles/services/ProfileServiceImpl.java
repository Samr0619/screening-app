package in.deloitte.screening.app.profiles.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import in.deloitte.screening.app.document.entities.Resume;
import in.deloitte.screening.app.document.entities.ResumeJobDescription;
import in.deloitte.screening.app.document.repositories.DocumentQueries;
import in.deloitte.screening.app.document.repositories.JobDescriptionRepository;
import in.deloitte.screening.app.document.repositories.ResumeJobDescriptionRespository;
import in.deloitte.screening.app.document.utils.DocumentTextExtractor;
import in.deloitte.screening.app.profiles.dto.ProfilesResponse;
import in.deloitte.screening.app.profiles.dto.SearchProfilesRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Service
public class ProfileServiceImpl implements ProfileService {

	private static final Logger logger = LogManager.getLogger(ProfileServiceImpl.class);

	@Autowired
	@Qualifier("pdf")
	private DocumentTextExtractor pdfTextExtractor;

	@Autowired
	@Qualifier("docx")
	private DocumentTextExtractor docxTextExtractor;


	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	private ResumeJobDescriptionRespository resumeJobDescriptionRespository;
	

	@Override
	public List<ProfilesResponse> matchingProfilesResponse(SearchProfilesRequest request) {
		
		List<ResumeJobDescription> resumeJobDescriptionList = resumeJobDescriptionRespository.getByJdFileName(request.jdFileName());
		List<String> resumeFileName = new ArrayList<>();
		for(ResumeJobDescription rjd : resumeJobDescriptionList) {
			resumeFileName.add(rjd.getResumeFileName());
		}
		
		@SuppressWarnings("unchecked")
		List<Resume> resumes = entityManager.createNativeQuery(DocumentQueries.createResumeResultsQuery(Double.parseDouble(request.minExp()), 
				Double.parseDouble(request.maxExp())), 
				Resume.class).getResultList();
		
		List<Resume> eligibleResumes = new ArrayList<>();
		for(Resume resume : resumes) {
			for(String fileName : resumeFileName) {
				if(fileName.equals(resume.getResumeFileName())) {
					eligibleResumes.add(resume);
				}
			}
		}
		
		List<ProfilesResponse> profiles = new ArrayList<>();
		for(int i =0; i < eligibleResumes.size(); i++) {
			
			for(int j = 0; j < resumeJobDescriptionList.size(); j++) {
				
				if(eligibleResumes.get(i).getResumeFileName().equals(
						resumeJobDescriptionList.get(j).getResumeFileName())) {
					
					Resume r = eligibleResumes.get(i);
					ResumeJobDescription rjd = resumeJobDescriptionList.get(j);
					ProfilesResponse profileResponse = new ProfilesResponse(r.getEmail(), r.getExperience(),
							r.getSkills(), r.getResumeFileName(), r.getUploadedBy(), r.getUploadTime(),
							r.getResumeFile(), r.getDownloads(), rjd.getJdFileName(), rjd.getCosineSimilarity()
							);
					profiles.add(profileResponse);
				}
			}
			
		}
		
		List<ProfilesResponse> responses =  profiles.stream()
				.sorted((o1, o2) -> o1.getCosineSimilarity().compareTo(o2.getCosineSimilarity())).collect(Collectors.toList());
		Collections.reverse(responses);

		return responses;
	}

}
