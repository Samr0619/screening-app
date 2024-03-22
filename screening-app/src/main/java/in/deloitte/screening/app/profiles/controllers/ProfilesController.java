package in.deloitte.screening.app.profiles.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import in.deloitte.screening.app.exceptions.BadInputException;
import in.deloitte.screening.app.profiles.dto.ProfilesResponse;
import in.deloitte.screening.app.profiles.dto.SearchProfilesRequest;
import in.deloitte.screening.app.profiles.services.ProfileService;
import in.deloitte.screening.app.utils.Mapper;


@RestController
@RequestMapping("/search")
public class ProfilesController {
	
	private static final Logger logger = LogManager.getLogger(ProfilesController.class);
	
	@Autowired
	ProfileService applicantService;
	
	/**
	 *  
	 * @param searchProfilesRequest
	 * @param jobDescriptionFile
	 * 
	 * @return Returns List of all matched applicant profiles
	 * 
	 * @throws IOException
	 * 
	 */
	@PostMapping("/profiles")
	public ResponseEntity<List<ProfilesResponse>> searchMatchingProfiles(
			@RequestBody SearchProfilesRequest request) {
		
		logger.info("Search profile request text : {}", Mapper.mapToJsonString(request));
		
		if(request.jdFileName() == null || request.jdFileName().length() == 0) {
			throw new BadInputException("Job Description file name is missing");
		}
		
		List<ProfilesResponse> results = applicantService.matchingProfilesResponse(request);
		
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}
}
