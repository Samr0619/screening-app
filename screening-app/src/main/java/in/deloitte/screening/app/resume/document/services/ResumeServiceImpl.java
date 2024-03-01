package in.deloitte.screening.app.resume.document.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.deloitte.screening.app.resume.document.dto.ResumeDownloadedUserRequest;
import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUserInfo;
import in.deloitte.screening.app.resume.document.repositories.ResumeRepository;

@Service
public class ResumeServiceImpl implements ResumeService {
	
	@Autowired
	ResumeRepository resumeRepository;

	@Transactional
	@Override
	public String saveDownloadedUserInfo(ResumeDownloadedUserRequest request) {
		
		ResumeDownloadedUserInfo resumeDownloadedUserInfo = new ResumeDownloadedUserInfo();
		resumeDownloadedUserInfo.setApplicantEmail(request.getApplicantEmail());
		resumeDownloadedUserInfo.setDownloadedUserEmail(request.getUserEmail());
		resumeDownloadedUserInfo.setDownloadTime(LocalDateTime.now());
		
		int resumeId = resumeRepository.getResumeData(request.getApplicantEmail()).getId();
		
		resumeRepository.saveDownloadedUserInfo(resumeId, request.getApplicantEmail(),
				LocalDateTime.now(), request.getUserEmail());
		
		return "Updated...";
	}

}
