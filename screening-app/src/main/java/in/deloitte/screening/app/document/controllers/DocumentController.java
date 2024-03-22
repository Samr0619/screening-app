package in.deloitte.screening.app.document.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.document.dto.UploadJdResponse;
import in.deloitte.screening.app.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.document.services.JobDescriptionService;
import in.deloitte.screening.app.document.services.ResumeService;
import in.deloitte.screening.app.exceptions.BadInputException;


@RestController
@RequestMapping("/upload")
public class DocumentController {
	
	private Logger logger = LogManager.getLogger(DocumentController.class);

	@Autowired
	ResumeService resumeService;
	
	@Autowired
	JobDescriptionService jobDescriptionService;
	
	@PostMapping("/resumes")
	public ResponseEntity<UploadResumesResponse> saveApplicantResume(
							@RequestPart("cv") List<MultipartFile> resumes, 
							@RequestPart("email") String userEmail) throws IOException{
	
		if(resumes.get(0).isEmpty()) {
			throw new BadInputException("No resumes found, Please upload atleast 1 resume");
		}
		else if(resumes.size() > 100) {
			throw new BadInputException("Maximum 100 resumes can be uploaded");
		}
		else if(userEmail.equals("\"\"") || userEmail.equals("null")) {
			throw new BadInputException("User email is required to upload Resume");
		}
		
		logger.info("Logged In User email : {} ", userEmail);
		UploadResumesResponse response = resumeService.saveResumeInfo(resumes, userEmail);		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
	@PostMapping("/jd")
	public ResponseEntity<UploadJdResponse> saveJobDescription(
							@RequestPart("jd") MultipartFile jd, 
							@RequestPart("email") String userEmail) throws IOException {
		
		if(jd.isEmpty()) {
			throw new BadInputException("No Job Description found, Please upload it");
		}
		else if(!jd.getOriginalFilename().endsWith(".pdf") && !jd.getOriginalFilename().endsWith(".docx")) {
			throw new BadInputException("Unsupported file format : " + jd.getOriginalFilename().substring(jd.getOriginalFilename().indexOf(".")+1).toUpperCase() + ", only pdf or docx are acceptable...");
		}
		else if(userEmail.equals("\"\"") || userEmail.equals("null")) {
			throw new BadInputException("User email is required to upload Job Description");
		}
		
		UploadJdResponse uploadJdResponse = jobDescriptionService.saveJobDescription(jd, userEmail);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(uploadJdResponse);
	}
	
}
