package in.deloitte.screening.app.document.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.deloitte.screening.app.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.document.services.JobDescriptionService;
import in.deloitte.screening.app.document.services.ResumeService;


@RestController
@RequestMapping("/resume")
public class ResumeController {
	
	@Autowired
	ResumeService resumeService;
	
	@Autowired
	JobDescriptionService jobDescriptionService;

	
	@PostMapping("/download/user")
	public ResponseEntity<String> saveResumeDownloadedUserInfo(@RequestBody ResumeDownloadedUserRequest request){
		
		String response = resumeService.saveDownloadedUserInfo(request);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	
}
