package in.deloitte.screening.app.applicant.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUserInfo;
import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUsers;


public class SearchProfilesResponse {
	
	public SearchProfilesResponse() {
		
	}

//	public SearchProfilesResponse(String email, double profileMatch, List<String> skills, LocalDateTime dateUploaded,
//			LocalDateTime shortListedDate, String shortListedBy) {
//		super();
//		this.email = email;
//		this.profileMatch = profileMatch;
//		this.skills = skills;
//		this.dateUploaded = dateUploaded;
//		this.shortListedDate = shortListedDate;
//		this.shortListedBy = shortListedBy;
//	}
	
	private String email;
	
	private double profileMatch;
	
	private Set<String> skills;
	
	private String experience;
	
	public SearchProfilesResponse(String experience) {
		super();
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

	private String uploadedBy;
	
	private LocalDateTime dateUploaded;
	
	private byte[] resume;
	
public SearchProfilesResponse(String email, double profileMatch, Set<String> skills, String uploadedBy,
			LocalDateTime dateUploaded, byte[] resume, List<ResumeDownloadedUsers> downloadedBy) {
		super();
		this.email = email;
		this.profileMatch = profileMatch;
		this.skills = skills;
		this.uploadedBy = uploadedBy;
		this.dateUploaded = dateUploaded;
		this.resume = resume;
		this.downloadedBy = downloadedBy;
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

	//	private LocalDateTime shortListedDate;
//	
//	private String shortListedBy;
	private List<ResumeDownloadedUsers> downloadedBy;
	
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
	public double getProfileMatch() {
		return profileMatch;
	}
	/**
	 * @param profileMatch the profileMatch to set
	 */
	public void setProfileMatch(double profileMatch) {
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
	 * @return the shortListedDate
	 */
//	public LocalDateTime getShortListedDate() {
//		return shortListedDate;
//	}
//	/**
//	 * @param shortListedDate the shortListedDate to set
//	 */
//	public void setShortListedDate(LocalDateTime shortListedDate) {
//		this.shortListedDate = shortListedDate;
//	}
//	/**
//	 * @return the shortListedBy
//	 */
//	public String getShortListedBy() {
//		return shortListedBy;
//	}
//	/**
//	 * @param shortListedBy the shortListedBy to set
//	 */
//	public void setShortListedBy(String shortListedBy) {
//		this.shortListedBy = shortListedBy;
//	}
//	
//	@Override
//	public String toString() {
//		return "SearchProfilesResponse [email=" + email + ", profileMatch=" + profileMatch + ", skills=" + skills
//				+ ", dateUploaded=" + dateUploaded + ", shortListedDate=" + shortListedDate + ", shortListedBy="
//				+ shortListedBy + "]";
//	}
	/**
	 * @return the downloadedBy
	 */
	public List<ResumeDownloadedUsers> getDownloadedBy() {
		return downloadedBy;
	}
	
	
	public SearchProfilesResponse(String email, double profileMatch, Set<String> skills, String uploadedBy,
			LocalDateTime dateUploaded, List<ResumeDownloadedUsers> downloadedBy) {
		super();
		this.email = email;
		this.profileMatch = profileMatch;
		this.skills = skills;
		this.uploadedBy = uploadedBy;
		this.dateUploaded = dateUploaded;
		this.downloadedBy = downloadedBy;
	}
//	@Override
//	public String toString() {
//		return "SearchProfilesResponse [email=" + email + ", profileMatch=" + profileMatch + ", skills=" + skills
//				+ ", uploadedBy=" + uploadedBy + ", dateUploaded=" + dateUploaded + ", downloadedBy=" + downloadedBy
//				+ "]";
//	}
	/**
	 * @return the uploadedBy
	 */
	public String getUploadedBy() {
		return uploadedBy;
	}
public SearchProfilesResponse(String email, double profileMatch, Set<String> skills, String experience,
			String uploadedBy, LocalDateTime dateUploaded, byte[] resume, List<ResumeDownloadedUsers> downloadedBy) {
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
	//	@Override
//	public String toString() {
//		return "SearchProfilesResponse [email=" + email + ", profileMatch=" + profileMatch + ", skills=" + skills
//				+ ", uploadedBy=" + uploadedBy + ", dateUploaded=" + dateUploaded + ", resume="
//				+ Arrays.toString(resume) + ", downloadedBy=" + downloadedBy + "]";
//	}
	/**
	 * @param uploadedBy the uploadedBy to set
	 */
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	/**
	 * @param downloadedBy the downloadedBy to set
	 */
	public void setDownloadedBy(List<ResumeDownloadedUsers> downloadedBy) {
		this.downloadedBy = downloadedBy;
	}
}
