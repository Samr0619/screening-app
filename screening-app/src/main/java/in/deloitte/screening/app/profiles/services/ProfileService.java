package in.deloitte.screening.app.profiles.services;

import java.util.List;

import org.springframework.stereotype.Service;

import in.deloitte.screening.app.profiles.dto.ProfilesResponse;
import in.deloitte.screening.app.profiles.dto.SearchProfilesRequest;


@Service
public interface ProfileService {
	
	public List<ProfilesResponse> matchingProfilesResponse(SearchProfilesRequest request);
}
