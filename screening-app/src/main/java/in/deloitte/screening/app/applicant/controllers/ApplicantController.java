package in.deloitte.screening.app.applicant.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.applicant.dto.SearchProfilesRequest;
import in.deloitte.screening.app.applicant.dto.SearchProfilesResponse;
import in.deloitte.screening.app.applicant.services.ApplicantService;
import in.deloitte.screening.app.exceptions.BadInputException;
import in.deloitte.screening.app.exceptions.DataNotFoundException;


@RestController
@RequestMapping("/applicant")
public class ApplicantController {
	
	@Autowired
	ApplicantService applicantService;
	

	@GetMapping("/search/matching/profiles")
	public ResponseEntity<List<SearchProfilesResponse>> searchProfiles(
											@RequestPart("text") SearchProfilesRequest searchProfilesRequest,
											@RequestPart(name = "file", required = false) MultipartFile jobDescriptionFile)
													throws IOException{
		
		if(searchProfilesRequest.getJobDescriptionText() == "" && jobDescriptionFile.isEmpty()) {
			throw new BadInputException("Job description is missing, Please send Job description as text OR file...");
		}
		
		List<SearchProfilesResponse> results = applicantService.matchingProfilesResponse(searchProfilesRequest,
																		jobDescriptionFile);
		if( results == null || results.size() == 0) {
			throw new DataNotFoundException("No data found for given input...");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}
}
