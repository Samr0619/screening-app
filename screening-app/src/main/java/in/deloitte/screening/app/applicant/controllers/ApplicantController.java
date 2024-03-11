package in.deloitte.screening.app.applicant.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import in.deloitte.screening.app.utils.Mapper;


@RestController
@RequestMapping("/applicant")
public class ApplicantController {
	
	private static final Logger logger = LogManager.getLogger(ApplicantController.class);
	
	@Autowired
	ApplicantService applicantService;
	
	/**
	 *  
	 * @param searchProfilesRequest
	 * @param jobDescriptionFile
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/profiles")
	public ResponseEntity<List<SearchProfilesResponse>> searchMatchingProfiles(
											@RequestPart("text") SearchProfilesRequest searchProfilesRequest,
											@RequestPart(name = "file", required = false) MultipartFile jobDescriptionFile)
													throws IOException{
		
		logger.info("Search profile request text : {}", Mapper.mapToJsonString(searchProfilesRequest));
		logger.info("Search profile request JD file : {}", jobDescriptionFile.getOriginalFilename());
		
		if(searchProfilesRequest.getJobDescriptionText().equals("") && jobDescriptionFile.isEmpty()) {
			throw new BadInputException("Job description is missing, Please send Job description as text OR file...");
		}
		
		logger.info("Going inside applicantService.matchingProfilesResponse(searchProfilesRequest,jobDescriptionFile)...");
		List<SearchProfilesResponse> results = applicantService.matchingProfilesResponse(searchProfilesRequest,
																		jobDescriptionFile);
		
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}
}
