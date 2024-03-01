package in.deloitte.screening.app.applicant.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.applicant.dto.SearchProfilesRequest;
import in.deloitte.screening.app.applicant.dto.SearchProfilesResponse;
import in.deloitte.screening.app.resume.document.dto.UploadResumesResponse;


@Service
public interface ApplicantService {
	
	public UploadResumesResponse saveApplicantInfo(List<MultipartFile> resume/*,  MultipartFile stopWords*/, String userEmail) throws IOException;
	
	public List<SearchProfilesResponse> matchingProfilesResponse(SearchProfilesRequest request,
			MultipartFile jobDescriptionFile) throws IOException;
}
