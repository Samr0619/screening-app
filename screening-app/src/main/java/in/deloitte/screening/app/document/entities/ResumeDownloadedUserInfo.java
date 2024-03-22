package in.deloitte.screening.app.document.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


@Embeddable
public class ResumeDownloadedUserInfo {
	
	@Column(name = "applicant_email")
	@JsonIgnore
	private String applicantEmail;

	@Column(name = "download_user_email")
	private String downloadedUserEmail;
	
	@Column(name = "download_time")
	private LocalDateTime downloadTime;
	
	
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
	
	public ResumeDownloadedUserInfo() {
		
	}
	
	public ResumeDownloadedUserInfo(String applicantEmail, String downloadedUserEmail, LocalDateTime downloadTime) {
		this.applicantEmail = applicantEmail;
		this.downloadedUserEmail = downloadedUserEmail;
		this.downloadTime = downloadTime;
	}

	public ResumeDownloadedUserInfo(String downloadedUserEmail, LocalDateTime downloadTime) {
		this.downloadedUserEmail = downloadedUserEmail;
		this.downloadTime = downloadTime;
	}
	

	@Override
	public String toString() {
		return "ResumeDownloadedUser [applicantEmail=" + applicantEmail + ", downloadedUserEmail=" + downloadedUserEmail
				+ ", downloadTime=" + downloadTime + "]";
	}
}
