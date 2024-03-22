package in.deloitte.screening.app.profiles.dto;

import java.time.LocalDateTime;


public class ResumeDownloads {

	private String downloadedUserEmail;
	
	private LocalDateTime downloadTime;

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

	public ResumeDownloads(String downloadedUserEmail, LocalDateTime downloadTime) {
		super();
		this.downloadedUserEmail = downloadedUserEmail;
		this.downloadTime = downloadTime;
	}

	@Override
	public String toString() {
		return "Resumedownloads [downloadedUserEmail=" + downloadedUserEmail + ", downloadTime=" + downloadTime + "]";
	}
	
}
