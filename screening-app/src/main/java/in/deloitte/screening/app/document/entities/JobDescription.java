package in.deloitte.screening.app.document.entities;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class JobDescription {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private int id;
	
	@Column(name = "requistion_id")
	private String requistionId;
	
//	@Column(name = "jd_file_name")
	@Id
	private String jdFileName;
	
	@Column(name = "jd_file")
	private byte [] jdFile;
	
	@Column(name = "jd_text", length = 100000)
	private String jdText;
	
	@Convert(converter = MapToJson.class)
	@Column(name = "jd_vector", length = 100000)
	private Map<String, Long> jdVector;
	
	@Column(name = "uploaded_by")
	private String uploadedBy;
	
	@Column(name = "upload_time")
	private LocalDateTime uploadTime;
	
//	@ManyToMany(mappedBy = "jd")
//	private Set<Resume> resumes = new HashSet<>();
	
//	@ManyToMany
//	@JoinTable(name = "res_jd", 
//	joinColumns = @JoinColumn(name = "jd_file_name", referencedColumnName = "jdFileName"),
//	inverseJoinColumns = @JoinColumn(name = "resume_id", referencedColumnName = "resume_id"))
//	private Set<ResumeJobDescription> resJd = new HashSet<>();
	

//	/**
//	 * @return the id
//	 */
//	public int getId() {
//		return id;
//	}
//
//	/**
//	 * @param id the id to set
//	 */
//	public void setId(int id) {
//		this.id = id;
//	}

	/**
	 * @return the requistionId
	 */
	public String getRequistionId() {
		return requistionId;
	}

	/**
	 * @param requistionId the requistionId to set
	 */
	public void setRequistionId(String requistionId) {
		this.requistionId = requistionId;
	}

	/**
	 * @return the fileName
	 */
	public String getJdFileName() {
		return jdFileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setJdFileName(String jdFileName) {
		this.jdFileName = jdFileName;
	}
	
	

	/**
	 * @return the jdFile
	 */
	public byte[] getJdFile() {
		return jdFile;
	}

	/**
	 * @param jdFile the jdFile to set
	 */
	public void setJdFile(byte[] jdFile) {
		this.jdFile = jdFile;
	}

	/**
	 * @return the jdText
	 */
	public String getJdText() {
		return jdText;
	}

	/**
	 * @param jdText the jdText to set
	 */
	public void setJdText(String jdText) {
		this.jdText = jdText;
	}

	/**
	 * @return the jdVector
	 */
	public Map<String, Long> getJdVector() {
		return jdVector;
	}

	/**
	 * @param jdVector the jdVector to set
	 */
	public void setJdVector(Map<String, Long> jdVector) {
		this.jdVector = jdVector;
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
	 * @return the resumes
	 */
//	public Set<Resume> getResumes() {
//		return resumes;
//	}
//
//	/**
//	 * @param resumes the resumes to set
//	 */
//	public void setResumes(Set<Resume> resumes) {
//		this.resumes = resumes;
//	}

	public JobDescription() {
		
	}

	public JobDescription(String requistionId, String jdFileName, byte [] jdFile, String jdText,
			Map<String, Long> jdVector, String uploadedBy, LocalDateTime uploadTime) {
		this.requistionId = requistionId;
		this.jdFileName = jdFileName;
		this.jdFile = jdFile;
		this.jdText = jdText;
		this.jdVector = jdVector;
		this.uploadedBy = uploadedBy;
		this.uploadTime = uploadTime;
	}

	@Override
	public String toString() {
		return "JobDescription [requistionId=" + requistionId + ", jdFileName=" + jdFileName + ", jdFile="
				+ Arrays.toString(jdFile) + ", jdText=" + jdText + ", jdVector=" + jdVector + ", uploadedBy="
				+ uploadedBy + ", uploadTime=" + uploadTime + ", resumes=" + "]";
	}
	
}
