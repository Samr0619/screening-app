package in.deloitte.screening.app.resume.document.entities;

import java.time.LocalDateTime;


public class ResumeDownloadedUsers {

//	private String applicantEmail;

	private String downloadedUserEmail;
	
	private LocalDateTime downloadTime;
	
	
	/**
	 * @return the applicantEmail
	 */
//	public String getApplicantEmail() {
//		return applicantEmail;
//	}
//
//	/**
//	 * @param applicantEmail the applicantEmail to set
//	 */
//	public void setApplicantEmail(String applicantEmail) {
//		this.applicantEmail = applicantEmail;
//	}

	/**
	 * @return the downloadedUserEmail
	 */
	public String getDownloadedUserEmail() {
		return downloadedUserEmail;
	}

	/**
	 * @param downloadedUserEmail the downloadedUserEmail to set
	 */
	public void setDownloadedUserEmail(String downloadedUserEmail) {
		this.downloadedUserEmail = downloadedUserEmail;
	}

	/**
	 * @return the downloadTime
	 */
	public LocalDateTime getDownloadTime() {
		return downloadTime;
	}

	/**
	 * @param downloadTime the downloadTime to set
	 */
	public void setDownloadTime(LocalDateTime downloadTime) {
		this.downloadTime = downloadTime;
	}
	
	public ResumeDownloadedUsers() {
		
	}
	
//	public ResumeDownloadedUsers(String applicantEmail, String downloadedUserEmail, LocalDateTime downloadTime) {
////		this.applicantEmail = applicantEmail;
//		this.downloadedUserEmail = downloadedUserEmail;
//		this.downloadTime = downloadTime;
//	}

	public ResumeDownloadedUsers(String downloadedUserEmail, LocalDateTime downloadTime) {
		this.downloadedUserEmail = downloadedUserEmail;
		this.downloadTime = downloadTime;
	}
	

	@Override
	public String toString() {
		return "ResumeDownloadedUser [downloadedUserEmail=" + downloadedUserEmail
				+ ", downloadTime=" + downloadTime + "]";
	}
	
}
