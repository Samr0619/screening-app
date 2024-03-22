package in.deloitte.screening.app.document.repositories;

import java.time.LocalDate;
import in.deloitte.screening.app.document.entities.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface ResumeRepository extends JpaRepository<Resume, Integer> {

    @Query(value = "SELECT * from public.resume WHERE email=:email", nativeQuery = true)
    public Resume getResumeData(String email);

    @Query(value = "SELECT * from public.resume ORDER BY email", nativeQuery = true)
    public List<Resume> getAllResumeData();

    @Modifying
    @Query(value = "INSERT INTO public.resume_downloads(\r\n"
            + "resume_id, applicant_email, download_time, download_user_email)\r\n"
            + "	VALUES (:resumeId, :applicantEmail, :time, :userEmail); ", nativeQuery = true)
    public void saveDownloadedUserInfo(int resumeId, String applicantEmail, LocalDateTime time, String userEmail);

    @Query(value = "SELECT id from public.resume WHERE email=:email", nativeQuery = true)
    public Optional<Integer> getResumeByEmail(@Param("email") String userEmail);

	@Query(value = "SELECT * FROM public.resume WHERE DATE(uploaded_time) BETWEEN :thirtyDaysAgoDate AND :currentDate", nativeQuery = true)
	public List<Resume> getResumesDataOfLast30Days(LocalDate thirtyDaysAgoDate, LocalDate currentDate);

	@Query(value = "SELECT * FROM public.resume WHERE experience BETWEEN :minExp AND :maxExp ORDER BY resume_file_name", nativeQuery = true)
	public List<Resume> getResumeDataByExperience(String minExp, String maxExp);
	
}
