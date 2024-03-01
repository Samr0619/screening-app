package in.deloitte.screening.app.skills.dto;

import java.util.List;

public class AddSkillsRequest {
	

	private List<String> skills;


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
	
	
	public AddSkillsRequest() {
		
	}

	public AddSkillsRequest(List<String> skills) {
		this.skills = skills;
	}
	
	
	@Override
	public String toString() {
		return "AddSkillsRequest [skills=" + skills + "]";
	}
	
}
