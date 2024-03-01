package in.deloitte.screening.app.resume.document.services;

import org.springframework.stereotype.Service;

import in.deloitte.screening.app.resume.document.dto.ResumeDownloadedUserRequest;

@Service
public interface ResumeService {

	public String saveDownloadedUserInfo(ResumeDownloadedUserRequest request);
}
