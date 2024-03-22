package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.document.dto.UploadResumesResponse;

@Service
public interface ResumeService {

	/**
	 * 
	 * @param resumes
	 * @param userEmail
	 * 
	 * @Returns UploadResumesResponse after saving data in Applicant and Resume tables
	 * 
	 * @throws IOException
	 * 
	 */
	public UploadResumesResponse saveResumeInfo(List<MultipartFile> resume/*,  MultipartFile stopWords*/, String userEmail) throws IOException;

	
	public String saveDownloadedUserInfo(ResumeDownloadedUserRequest request);
}
