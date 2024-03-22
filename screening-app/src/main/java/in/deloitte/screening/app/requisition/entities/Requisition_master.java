package in.deloitte.screening.app.requisition.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "requisition_master")
public class Requisition_master {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "requis_code")
	private String requis_code;
	
	@Column(name = "job_title")
	private String job_title;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRequis_code() {
		return requis_code;
	}
	public void setRequis_code(String requis_code) {
		this.requis_code = requis_code;
	}
	public String getJob_title() {
		return job_title;
	}
	public void setJob_title(String job_title) {
		this.job_title = job_title;
	}
	
	
	
}
