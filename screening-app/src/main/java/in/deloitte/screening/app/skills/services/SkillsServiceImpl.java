package in.deloitte.screening.app.skills.services;

import java.util.ArrayList;
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
		
		List<Skills> skillList = new ArrayList<>();
		for(String s : addSkillsRequest.getSkills()) {
			Skills skill = new Skills(s);
			skillList.add(skill);
		}
//		System.out.println("skillList : " + skillList);
		return skillsRepository.saveAll(skillList);
	}

}
