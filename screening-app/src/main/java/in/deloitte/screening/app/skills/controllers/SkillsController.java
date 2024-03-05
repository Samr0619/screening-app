package in.deloitte.screening.app.skills.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.deloitte.screening.app.skills.dto.AddSkillsRequest;
import in.deloitte.screening.app.skills.entities.Skills;
import in.deloitte.screening.app.skills.services.SkillsService;


@RestController
@RequestMapping("/skills")
public class SkillsController {
	
	@Autowired
	SkillsService skillsService;

	@PostMapping("/add")
	public ResponseEntity<List<Skills>> createSkills(@RequestBody AddSkillsRequest addSkillsRequest){
		
		List<Skills> addedSkills = skillsService.addSkills(addSkillsRequest);
		return ResponseEntity.status(HttpStatus.OK).body(addedSkills);
	}
}
