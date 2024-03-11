package in.deloitte.screening.app.resume.document.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.applicant.services.ApplicantService;
import in.deloitte.screening.app.exceptions.BadInputException;
import in.deloitte.screening.app.resume.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.resume.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.resume.document.services.ResumeService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/resumes")
public class ResumeDocumentController {
	
	 private Logger logger = LogManager.getLogger(ResumeDocumentController.class);
	
	@Autowired
	ApplicantService applicantService;
	
	@Autowired
	ResumeService resumeService;
	
	@PostMapping("/upload")
	public ResponseEntity<UploadResumesResponse> saveApplicantResume(
							@RequestPart("cv") List<MultipartFile> resumes, 
							@RequestPart("email") String userEmail) throws IOException{
		
		if(resumes.get(0).isEmpty()) {
			throw new BadInputException("No resumes found, Please upload atleast 1 resume");
		}
		
		if(resumes.size() > 100) {
			throw new BadInputException("Maximum 100 resumes can be uploaded");
		}
		
		if(userEmail.equals("\"\"") || userEmail == null) {
			throw new BadInputException("User email is required to upload");
		}
		logger.info("Logged In User email : {} ",userEmail);
		UploadResumesResponse response = applicantService.saveApplicantInfo(resumes, userEmail);		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/save/download/user")
	public ResponseEntity<String> downloadResume(@RequestBody ResumeDownloadedUserRequest request){
		
		String response = resumeService.saveDownloadedUserInfo(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}
