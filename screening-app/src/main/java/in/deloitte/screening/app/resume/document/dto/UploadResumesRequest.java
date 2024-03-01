package in.deloitte.screening.app.resume.document.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class UploadResumesRequest {

	private String uploadedUserEmail;
	
	private List<MultipartFile> resumes;

	/**
	 * @return the uploadedUserEmail
	 */
	public String getUploadedUserEmail() {
		return uploadedUserEmail;
	}

	/**
	 * @param uploadedUserEmail the uploadedUserEmail to set
	 */
	public void setUploadedUserEmail(String uploadedUserEmail) {
		this.uploadedUserEmail = uploadedUserEmail;
	}

	/**
	 * @return the resumes
	 */
	public List<MultipartFile> getResumes() {
		return resumes;
	}

	/**
	 * @param resumes the resumes to set
	 */
	public void setResumes(List<MultipartFile> resumes) {
		this.resumes = resumes;
	}
	
	public UploadResumesRequest() {
		
	}

	public UploadResumesRequest(String uploadedUserEmail, List<MultipartFile> resumes) {
		this.uploadedUserEmail = uploadedUserEmail;
		this.resumes = resumes;
	}

	@Override
	public String toString() {
		return "UploadResumesRequest [uploadedUserEmail=" + uploadedUserEmail + ", resumes=" + resumes + "]";
	}
	
	
}
