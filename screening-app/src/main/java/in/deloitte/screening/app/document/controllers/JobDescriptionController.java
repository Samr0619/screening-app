package in.deloitte.screening.app.document.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import in.deloitte.screening.app.document.dto.SearchJdResponse;
import in.deloitte.screening.app.document.services.JobDescriptionService;


@RestController
@RequestMapping("/jd")
public class JobDescriptionController {

	@Autowired
	JobDescriptionService jobDescriptionService;
	
//	@GetMapping("/search/{title}")
//	public ResponseEntity<SearchJdResponse> searchJobDescriptionByTitle(@PathVariable String jdTitle){	
//		return null;
//	}
	
	@GetMapping("/search/{jdFileName}")
	public ResponseEntity<List<SearchJdResponse>> searchJobDescriptionByTitle(@PathVariable String jdFileName){
		return ResponseEntity.status(HttpStatus.OK).body(jobDescriptionService.getAllJdInfo(jdFileName));
	}
}
