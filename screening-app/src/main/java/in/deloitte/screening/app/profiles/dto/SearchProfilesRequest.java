package in.deloitte.screening.app.profiles.dto;

import java.util.List;


public record SearchProfilesRequest(String jdFileName, List<String> skills,
		String minExp, String maxExp, String fromDate, String toDate) {

}
