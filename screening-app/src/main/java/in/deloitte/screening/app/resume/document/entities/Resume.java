package in.deloitte.screening.app.resume.document.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Resume {

	public Resume(int id, String email, String text, Map<String, Long> vector) {
		super();
		this.id = id;
		this.email = email;
		this.text = text;
		this.vector = vector;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;
	
	@Column(name = "resume_file")
	private byte [] resumeFile;
	
	private String docType;
	
	@Column(name = "uploaded_by")
	private String uploadedBy;
	
	@Column(name = "uploaded_time")
	private LocalDateTime uploadTime;
	
	@Column(length = 100000)
	private String text;
	
	@Convert(converter = MapToJson.class)
	@Column(length = 100000)
	private Map<String, Long> vector;
	
	@ElementCollection
//	@Column(name = "downloads")
	private List<ResumeDownloadedUserInfo> downloads;
	
	public Resume(String email, byte[] resumeFile, String docType, String text, Map<String, Long> vector) {
		super();
		this.email = email;
		this.resumeFile = resumeFile;
		this.docType = docType;
		this.text = text;
		this.vector = vector;
	}

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

	public Resume(String email, byte[] resumeFile, String text, Map<String, Long> vector) {
		super();
		this.email = email;
		this.resumeFile = resumeFile;
		this.text = text;
		this.vector = vector;
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
	
	public Resume() {
		
	}

	public Resume(int id, String email, byte[] resumeFile, String docType, String uploadedBy, LocalDateTime uploadTime,
			String text, Map<String, Long> vector) {
		super();
		this.id = id;
		this.email = email;
		this.resumeFile = resumeFile;
		this.docType = docType;
		this.uploadedBy = uploadedBy;
		this.uploadTime = uploadTime;
		this.text = text;
		this.vector = vector;
	}

	@Override
	public String toString() {
		return "Resume [id=" + id + ", email=" + email + ", resumeFile=" + Arrays.toString(resumeFile) + ", docType="
				+ docType + ", uploadedBy=" + uploadedBy + ", uploadTime=" + uploadTime + ", text=" + text + ", vector="
				+ vector + "]";
	}
}
