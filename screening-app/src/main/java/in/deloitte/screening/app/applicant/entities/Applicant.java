package in.deloitte.screening.app.applicant.entities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUserInfo;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Applicant {

	@Id
	private String email;
	
	private String name;
	
	private String experience;
	
	@Column(name = "job_title")
	private String jobTitle;
	
	@Column(name = "preffered_locations")
	private List<String> locations;
	
	@Column(name = "uploaded_by")
	private String uploadedBy;
	
	@Column(name = "uploaded_time")
	private LocalDateTime uploadTime;
	

	private Set<String> skills;
	
//	@ElementCollection
//	@Column(name = "downloads")
//	private List<ResumeDownloadedUser> downloads;
//	

	/**
	 * @return the jobTitle
	 */
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle the jobTitle to set
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
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
	
//	/**
//	 * @return the downloadedBy
//	 */
//	public List<ResumeDownloadedUser> getDownloads() {
//		return downloads;
//	}
//
//	/**
//	 * @param downloadedBy the downloadedBy to set
//	 */
//	public void setDownloadedBy(List<ResumeDownloadedUser> downloads) {
//		this.downloads = downloads;
//	}

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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the time
	 */
	public LocalDateTime getUploadTime() {
		return uploadTime;
	}


	/**
	 * @param time the time to set
	 */
	public void setUploadTime(LocalDateTime uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @return the locations
	 */
	public List<String> getLocations() {
		return locations;
	}


	/**
	 * @param locations the locations to set
	 */
	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
	
	public Applicant() {
		
	}
	
	public Applicant(String email, String name, String experience, String jobTitle, List<String> locations,
			String uploadedBy, LocalDateTime uploadTime, Set<String> skills) {
		super();
		this.email = email;
		this.name = name;
		this.experience = experience;
		this.jobTitle = jobTitle;
		this.locations = locations;
		this.uploadedBy = uploadedBy;
		this.uploadTime = uploadTime;
		this.skills = skills;
	}

	@Override
	public String toString() {
		return "Applicant [name=" + name + ", email=" + email + ", experience=" + experience + ", jobTitle=" + jobTitle
				+ ", locations=" + locations + ", uploadedBy=" + uploadedBy
				+ ", uploadTime=" + uploadTime + ", skills=" + skills + "]";
	}
}
