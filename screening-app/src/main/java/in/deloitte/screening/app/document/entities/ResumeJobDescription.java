package in.deloitte.screening.app.document.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "resume_jd")
public class ResumeJobDescription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
//	@ManyToOne
//	@JoinColumn(name = "resume_id")
	@Column(name = "resume_id")
	private int resumeId;
	
//	@ManyToOne
//	@JoinColumn(name = "jobDescription_id")
	private int jdId;
	
	private String resumeName;
	
	private double cosineSimilarity;

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
	 * @return the resume_id
	 */
	public int getResumeId() {
		return resumeId;
	}

	/**
	 * @param resume_id the resume_id to set
	 */
	public void setResumeId(int resume_id) {
		this.resumeId = resume_id;
	}

	/**
	 * @return the jd_id
	 */
	public int getJdId() {
		return jdId;
	}

	/**
	 * @param jd_id the jd_id to set
	 */
	public void setJdId(int jd_id) {
		this.jdId = jd_id;
	}

	/**
	 * @return the resume_name
	 */
	public String getResumeName() {
		return resumeName;
	}

	/**
	 * @param resume_name the resume_name to set
	 */
	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}

	/**
	 * @return the cosineSimilarity
	 */
	public double getCosineSimilarity() {
		return cosineSimilarity;
	}

	/**
	 * @param cosineSimilarity the cosineSimilarity to set
	 */
	public void setCosineSimilarity(double cosineSimilarity) {
		this.cosineSimilarity = cosineSimilarity;
	}
	
	
	public ResumeJobDescription() {
		
	}

	public ResumeJobDescription(int resumeId, int jdId, String resumeName, double cosineSimilarity) {
		super();
		this.resumeId = resumeId;
		this.jdId = jdId;
		this.resumeName = resumeName;
		this.cosineSimilarity = cosineSimilarity;
	}

	@Override
	public String toString() {
		return "ResumeJD [id=" + id + ", resume_id=" + resumeId + ", jd_id=" + jdId + ", resume_name=" + resumeName
				+ ", cosineSimilarity=" + cosineSimilarity + "]";
	}
	
}
