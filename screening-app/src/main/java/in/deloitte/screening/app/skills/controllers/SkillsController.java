package in.deloitte.screening.app.skills.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/get/all")
	public ResponseEntity<List<String>> getSkills(){
		
		List<String> allSkills = skillsService.getAllSkills();
		return ResponseEntity.status(HttpStatus.OK).body(allSkills);
	}
	
	@GetMapping("/get/search-skill")
	public ResponseEntity<Map<String,Object>> getSearchSkill(@RequestParam String skill){
		Map<String,Object> map = new HashMap<String, Object>();
		List<Skills> allSkills = skillsService.findBySkillName(skill);
		map.put("list", allSkills);
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
}
