package in.deloitte.screening.app.resume.document.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.deloitte.screening.app.resume.document.entities.Resume;
import in.deloitte.screening.app.resume.document.entities.ResumeDownloadedUserInfo;


@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer>{
	
	@Query(value = "SELECT * from public.resume WHERE email=:email", nativeQuery = true)
	public Resume getResumeData(String email);
	
	@Modifying
	@Query(value = "INSERT INTO public.resume_downloads(\r\n"
			+ "resume_id, applicant_email, download_time, download_user_email)\r\n"
			+ "	VALUES (:resumeId, :applicantEmail, :time, :userEmail); ", nativeQuery = true)
	public void saveDownloadedUserInfo(int resumeId, String applicantEmail, LocalDateTime time, String userEmail);

	@Query(value = "SELECT * from public.resume WHERE email=:email", nativeQuery = true)
	public Resume getDownloadedUserByApplicantEmail(String email);

	@Query(value = "SELECT id from public.resume WHERE email=:email", nativeQuery = true)
	public Optional<Integer> getResumeByEmail(@Param("email")String userEmail);
	
}
