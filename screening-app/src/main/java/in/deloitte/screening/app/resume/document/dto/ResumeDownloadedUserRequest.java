package in.deloitte.screening.app.resume.document.dto;


public class ResumeDownloadedUserRequest {

	private String userEmail;
	
	private String applicantEmail;

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the applicantEmail
	 */
	public String getApplicantEmail() {
		return applicantEmail;
	}

	/**
	 * @param applicantEmail the applicantEmail to set
	 */
	public void setApplicantEmail(String applicantEmail) {
		this.applicantEmail = applicantEmail;
	}
	
	public ResumeDownloadedUserRequest() {
		
	}

	public ResumeDownloadedUserRequest(String userEmail, String applicantEmail) {
		super();
		this.userEmail = userEmail;
		this.applicantEmail = applicantEmail;
	}

	@Override
	public String toString() {
		return "ResumeDownloadedUserRequest [userEmail=" + userEmail + ", applicantEmail=" + applicantEmail +"]";
	}
	
}
