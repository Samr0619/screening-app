package in.deloitte.screening.app.skills.services;

import java.util.List;

import org.springframework.stereotype.Service;

import in.deloitte.screening.app.skills.dto.AddSkillsRequest;
import in.deloitte.screening.app.skills.entities.Skills;


@Service
public interface SkillsService {

	public List<Skills> addSkills(AddSkillsRequest addSkillsRequest);
	
	public List<String> getAllSkills();
}
