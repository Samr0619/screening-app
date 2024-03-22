package in.deloitte.screening.app.document.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.deloitte.screening.app.document.entities.ResumeJobDescription;


@Repository
public interface ResumeJobDescriptionRespository extends  JpaRepository<ResumeJobDescription, Integer> {

	@Query(value = "SELECT * from public.resume_job_description WHERE jd_file_name = :jdFileName ORDER BY resume_file_name", nativeQuery = true)
	List<ResumeJobDescription> getByJdFileName(String jdFileName);

}
