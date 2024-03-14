package in.deloitte.screening.app.skills.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.deloitte.screening.app.skills.entities.Skills;

public interface SkillsRepository extends JpaRepository<Skills, Integer>{
	
	@Query(value = "SELECT skill_name FROM skills_schema.skills", nativeQuery = true)
	public List<String> getAllSkills();

	@Query(value = "SELECT * FROM skills_schema.skills WHERE skill_name LIKE %:skill%", nativeQuery = true)
	public List<Skills> findBySkillName(String skill);
}
