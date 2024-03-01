package in.deloitte.screening.app.resume.document.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.applicant.services.ApplicantService;
import in.deloitte.screening.app.resume.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.resume.document.dto.UploadResumesRequest;
import in.deloitte.screening.app.resume.document.dto.UploadResumesResponse;
import in.deloitte.screening.app.resume.document.services.ResumeService;


@RestController
@RequestMapping("/resumes")
public class ResumeDocumentController {
	
	 private Logger logger = LoggerFactory.getLogger(ResumeDocumentController.class);
	
	@Autowired
	ApplicantService applicantService;
	
	@Autowired
	ResumeService resumeService;
	
	@PostMapping("/upload")
	public ResponseEntity<UploadResumesResponse> saveApplicantResume(
							@RequestParam("cv") List<MultipartFile> resumes, String userEmail) throws IOException{
		
//		UploadResumesResponse response = applicantService.saveApplicantInfo(resumes, userEmail);
		UploadResumesResponse response = applicantService.saveApplicantInfo(resumes, "mohp@deloitte.com");
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@PostMapping("/save/download/user")
	public ResponseEntity<String> downloadResume(@RequestBody ResumeDownloadedUserRequest request){
		
		String response = resumeService.saveDownloadedUserInfo(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
}
