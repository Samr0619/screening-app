package in.deloitte.screening.app.skills.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.deloitte.screening.app.skills.dto.AddSkillsRequest;
import in.deloitte.screening.app.skills.entities.Skills;
import in.deloitte.screening.app.skills.repositories.SkillsRepository;


@Service
public class SkillsServiceImpl implements SkillsService {
	
	@Autowired
	SkillsRepository skillsRepository;

	@Override
	public List<Skills> addSkills(AddSkillsRequest addSkillsRequest) {
	

		List<Skills> skillList =  addSkillsRequest.getSkills()
												  .stream()
												  .map(skill -> new Skills(skill)).toList();
		return skillsRepository.saveAll(skillList);
	}

	@Override
	public List<String> getAllSkills() {
		
		return skillsRepository.findAll()
							   .stream()
							   .map(skill -> skill.getSkillName())
							   .distinct()
							   .toList();
	}
	
}
