package in.deloitte.screening.app.applicant.repositories;

import java.util.stream.Collectors;

import in.deloitte.screening.app.applicant.dto.SearchProfilesRequest;


public class ApplicantQueries {
	
	public static String createApplicantResultsQuery(SearchProfilesRequest request) {
		
		String queryConstant = "SELECT * FROM public.applicant WHERE ";
		String prefix = "'";
		String suffix = "' = ANY(skills) AND ";
		String values = request.getSkills().stream()
							  .map(skill -> prefix+skill+suffix).collect(Collectors.joining());

		String query = queryConstant + values.substring(0, values.length()-5);
	
		return queryConstant + values + "job_title = '" + request.getJobTitle() + "'" + " AND DATE(uploaded_time) between '" + request.getFromDate() + "' AND '" + request.getToDate() + "' ORDER BY email";
	}
	
}
