package in.deloitte.screening.app.document.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Resume {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique = true)
	private String email;
	
	private Double experience;
	
	@Column(name = "resume_file")
	private byte [] resumeFile;
	
	private String resumeFileName;
	
	private String docType;
	
	@Column(name = "uploaded_by")
	private String uploadedBy;
	
	@Column(name = "uploaded_time")
	private LocalDateTime uploadTime;
	
	@Column(length = 100000)
	private String text;
	
	@Column(name = "resume_skills")
	private Set<String> skills;
	
	@Convert(converter = MapToJson.class)
	@Column(length = 100000)
	private Map<String, Long> vector;
	
	@ElementCollection
	private List<ResumeDownloadedUserInfo> downloads;
	
//	@ManyToMany
//	@JoinTable(name = "resume_jd", 
//	joinColumns = @JoinColumn(name = "resume_id", referencedColumnName = "id"),
//	inverseJoinColumns = @JoinColumn(name = "jobDescription_id", referencedColumnName = "jdFileName"))
//	private Set<JobDescription> jd = new HashSet<>();
	

	/**
	 * @return the docType
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * @param docType the docType to set
	 */
	public void setDocType(String docType) {
		this.docType = docType;
	}

	/**
	 * @return the experience
	 */
	public Double getExperience() {
		return experience;
	}

	/**
	 * @param experience the experience to set
	 */
	public void setExperience(Double experience) {
		this.experience = experience;
	}

	/**
	 * @return the resumeFile
	 */
	public byte[] getResumeFile() {
		return resumeFile;
	}

	/**
	 * @param resumeFile the resumeFile to set
	 */
	public void setResumeFile(byte[] resumeFile) {
		this.resumeFile = resumeFile;
	}

	/**
	 * @return the resumeFileName
	 */
	public String getResumeFileName() {
		return resumeFileName;
	}

	/**
	 * @param resumeFileName the resumeFileName to set
	 */
	public void setResumeFileName(String resumeFileName) {
		this.resumeFileName = resumeFileName;
	}

	/**
	 * @return the vector
	 */
	public Map<String, Long> getVector() {
		return vector;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
	 * @return the uploadTime
	 */
	public LocalDateTime getUploadTime() {
		return uploadTime;
	}

	/**
	 * @param uploadTime the uploadTime to set
	 */
	public void setUploadTime(LocalDateTime uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * @param downloads the downloads to set
	 */
	public void setDownloads(List<ResumeDownloadedUserInfo> downloads) {
		this.downloads = downloads;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * @return the tokens
	 */
	public Map<String, Long> getVectors() {
		return vector;
	}

	/**
	 * @param tokens the tokens to set
	 */
	public void setVector(Map<String, Long> vector) {
		this.vector = vector;
	}
	
	/**
	 * @return the downloadedBy
	 */
	public List<ResumeDownloadedUserInfo> getDownloads() {
		return downloads;
	}

	/**
	 * @param downloadedBy the downloadedBy to set
	 */
	public void setDownloadedBy(List<ResumeDownloadedUserInfo> downloads) {
		this.downloads = downloads;
	}
	
	/**
	 * @return the jd
	 */
//	public Set<JobDescription> getJd() {
//		return jd;
//	}
//
//	/**
//	 * @param jd the jd to set
//	 */
//	public void setJd(Set<JobDescription> jd) {
//		this.jd = jd;
//	}

	public Resume() {
		
	}

	public Resume(int id, String email, Double experience, byte[] resumeFile, String resumeFileName, String docType,
			String uploadedBy, LocalDateTime uploadTime, String text, Set<String> skills, Map<String, Long> vector,
			List<ResumeDownloadedUserInfo> downloads) {
		super();
		this.id = id;
		this.email = email;
		this.experience = experience;
		this.resumeFile = resumeFile;
		this.resumeFileName = resumeFileName;
		this.docType = docType;
		this.uploadedBy = uploadedBy;
		this.uploadTime = uploadTime;
		this.text = text;
		this.skills = skills;
		this.vector = vector;
		this.downloads = downloads;
	}
	

	@Override
	public String toString() {
		return "Resume [id=" + id + ", email=" + email + ", experience=" + experience + ", resumeFile="
				+ Arrays.toString(resumeFile) + ", resumeFileName=" + resumeFileName + ", docType=" + docType
				+ ", uploadedBy=" + uploadedBy + ", uploadTime=" + uploadTime + ", text=" + text + ", skills=" + skills
				+ ", vector=" + vector + ", downloads=" + downloads + "]";
	}

}
