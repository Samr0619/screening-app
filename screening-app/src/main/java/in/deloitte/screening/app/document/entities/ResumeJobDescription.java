package in.deloitte.screening.app.document.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class ResumeJobDescription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String resumeFileName;
	
	private String jdFileName;
	
	private Double cosineSimilarity;

	
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
	 * @return the jdFileName
	 */
	public String getJdFileName() {
		return jdFileName;
	}

	/**
	 * @param jdFileName the jdFileName to set
	 */
	public void setJdFileName(String jdFileName) {
		this.jdFileName = jdFileName;
	}

	/**
	 * @return the cosineSimilarity
	 */
	public Double getCosineSimilarity() {
		return cosineSimilarity;
	}

	/**
	 * @param cosineSimilarity the cosineSimilarity to set
	 */
	public void setCosineSimilarity(Double cosineSimilarity) {
		this.cosineSimilarity = cosineSimilarity;
	}

	public ResumeJobDescription() {
		
	}

	public ResumeJobDescription(String resumeFileName, String jdFileName, Double cosineSimilarity) {
		this.resumeFileName = resumeFileName;
		this.jdFileName = jdFileName;
		this.cosineSimilarity = cosineSimilarity;
	}

	@Override
	public String toString() {
		return "ResumeJobDescription [id=" + id + ", resumeFileName=" + resumeFileName + ", jdFileName=" + jdFileName
				+ ", cosineSimilarity=" + cosineSimilarity + "]";
	}
	
}
