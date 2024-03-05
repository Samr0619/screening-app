package in.deloitte.screening.app.applicant.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class SearchProfilesRequest {
	
	public SearchProfilesRequest() {
		
	}

	public SearchProfilesRequest(String jobTitle, List<String> skills, MultipartFile jobDescriptionFile,
			String jobDescriptionText) {
		super();
		this.jobTitle = jobTitle;
		this.skills = skills;
		this.jobDescriptionFile = jobDescriptionFile;
		this.jobDescriptionText = jobDescriptionText;
	}
	
	private String jobTitle;
	
	private List<String> skills;
	
	private MultipartFile jobDescriptionFile;
	
	private String jobDescriptionText;
	
	private String fromDate;
	
	private String toDate;
	
	
	
//	private 
	
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
	public List<String> getSkills() {
		return skills;
	}
	/**
	 * @param skills the skills to set
	 */
	public void setSkills(List<String> skills) {
		this.skills = skills;
	}
	/**
	 * @return the jobDescriptionFile
	 */
	public MultipartFile getJobDescriptionFile() {
		return jobDescriptionFile;
	}
	/**
	 * @param jobDescriptionFile the jobDescriptionFile to set
	 */
	public void setJobDescriptionFile(MultipartFile jobDescriptionFile) {
		this.jobDescriptionFile = jobDescriptionFile;
	}
	/**
	 * @return the jobDescriptionText
	 */
	public String getJobDescriptionText() {
		return jobDescriptionText;
	}
	/**
	 * @param jobDescriptionText the jobDescriptionText to set
	 */
	public void setJobDescriptionText(String jobDescriptionText) {
		this.jobDescriptionText = jobDescriptionText;
	}
	
	
	
	/**
	 * @return the fromDate
	 */
	public String getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public String getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "SearchProfilesRequest [jobTitle=" + jobTitle + ", skills=" + skills + ", jobDescriptionFile="
				+ jobDescriptionFile + ", jobDescriptionText=" + jobDescriptionText + "]";
	}
}
