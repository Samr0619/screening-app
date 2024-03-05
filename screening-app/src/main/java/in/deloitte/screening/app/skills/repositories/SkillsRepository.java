package in.deloitte.screening.app.skills.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.deloitte.screening.app.skills.entities.Skills;

public interface SkillsRepository extends JpaRepository<Skills, Integer>{
	
	@Query(value = "SELECT skill_name FROM skills_schema.skills", nativeQuery = true)
	public List<String> getAllSkills();

}
