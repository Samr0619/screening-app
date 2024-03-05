package in.deloitte.screening.app.applicant.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUserInfo;

public class SearchProfilesResponse {

	private String email;

	private Double profileMatch;

	private Set<String> skills;

	private String experience;

	private String uploadedBy;

	private LocalDateTime dateUploaded;

	private byte[] resume;
	
	private List<ResumeDownloadedUserInfo> downloadedBy;

	public SearchProfilesResponse(String experience) {
		this.experience = experience;
	}

	/**
	 * @return the experience
	 */
	public String getExperience() {
		return experience;
	}

	/**
	 * @param experience the experience to set
	 */
	public void setExperience(String experience) {
		this.experience = experience;
	}

	/**
	 * @return the resume
	 */
	public byte[] getResume() {
		return resume;
	}

	/**
	 * @param resume the resume to set
	 */
	public void setResume(byte[] resume) {
		this.resume = resume;
	}


	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the profileMatch
	 */
	public Double getProfileMatch() {
		return profileMatch;
	}

	/**
	 * @param profileMatch the profileMatch to set
	 */
	public void setProfileMatch(Double profileMatch) {
		this.profileMatch = profileMatch;
	}

	/**
	 * @return the skills
	 */
	public Set<String> getSkills() {
		return skills;
	}

	/**
	 * @param skills the skills to set
	 */
	public void setSkills(Set<String> skills) {
		this.skills = skills;
	}

	/**
	 * @return the dateUploaded
	 */
	public LocalDateTime getDateUploaded() {
		return dateUploaded;
	}

	/**
	 * @param dateUploaded the dateUploaded to set
	 */
	public void setDateUploaded(LocalDateTime dateUploaded) {
		this.dateUploaded = dateUploaded;
	}
	
	/**
	 * @return the downloadedBy
	 */
	public List<ResumeDownloadedUserInfo> getDownloadedBy() {
		return downloadedBy;
	}
	
	/**
	 * @param downloadedBy the downloadedBy to set
	 */
	public void setDownloadedBy(List<ResumeDownloadedUserInfo> downloadedBy) {
		this.downloadedBy = downloadedBy;
	}

	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}
	
	/**
	 * @param uploadedBy the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	

	public SearchProfilesResponse() {

	}

	

	public SearchProfilesResponse(String email, Double profileMatch, Set<String> skills, String experience,
			String uploadedBy, LocalDateTime dateUploaded, byte[] resume, List<ResumeDownloadedUserInfo> downloadedBy) {
		super();
		this.email = email;
		this.profileMatch = profileMatch;
		this.skills = skills;
		this.experience = experience;
		this.uploadedBy = uploadedBy;
		this.dateUploaded = dateUploaded;
		this.resume = resume;
		this.downloadedBy = downloadedBy;
	}

	@Override
	public String toString() {
		return "SearchProfilesResponse [email=" + email + ", profileMatch=" + profileMatch + ", skills=" + skills
				+ ", experience=" + experience + ", uploadedBy=" + uploadedBy + ", dateUploaded=" + dateUploaded
				+ ", resume=" + Arrays.toString(resume) + ", downloadedBy=" + downloadedBy + "]";
	}
	
}
