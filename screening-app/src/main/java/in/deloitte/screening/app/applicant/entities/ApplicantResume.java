package in.deloitte.screening.app.applicant.entities;

import in.deloitte.screening.app.document.entities.Resume;


public class ApplicantResume {

	private Applicant applicant;
	
	private Resume resume;

	/**
	 * @return the applicant
	 */
	public Applicant getApplicant() {
		return applicant;
	}

	/**
	 * @param applicant the applicant to set
	 */
	public void setApplicant(Applicant applicant) {
		this.applicant = applicant;
	}

	/**
	 * @return the resume
	 */
	public Resume getResume() {
		return resume;
	}

	/**
	 * @param resume the resume to set
	 */
	public void setResume(Resume resume) {
		this.resume = resume;
	}

	
	public ApplicantResume() {
		
	}
	
	public ApplicantResume(Applicant applicant, Resume resume) {
		super();
		this.applicant = applicant;
		this.resume = resume;
	}

	@Override
	public String toString() {
		return "ApplicantResume [applicant=" + applicant + ", resume=" + resume + "]";
	}
	
	
}
