package in.deloitte.screening.app.applicant.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import in.deloitte.screening.app.applicant.entities.Applicant;


@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Integer>{
	
	@Modifying
	@Query(value = "INSERT INTO public.applicant_downloaded_by(\r\n"
			+ "	applicant_email, download_time, downloaded_by)\r\n"
			+ "	VALUES (:email, :time, :user); ", nativeQuery = true)
	public void saveDownloadedUserInfo(String user, LocalDateTime time, String email);
	
	@Query(value = "SELECT resume FROM public.applicant WHERE email=:email", nativeQuery = true)
	public byte[] getResumeByEmail(String email);

}