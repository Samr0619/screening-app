package in.deloitte.screening.app.document.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.deloitte.screening.app.document.entities.JobDescription;


@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, Integer> {

	public Optional<JobDescription> findByJdFileName(String jdFileName);

}
