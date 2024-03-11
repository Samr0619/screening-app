package in.deloitte.screening.app.user.repositories;

import in.deloitte.screening.app.user.entities.LoginTable;
import in.deloitte.screening.app.user.entities.SignUpTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpRepository extends JpaRepository<SignUpTable, Long> {
    Optional<SignUpTable> findByEmail(String email);

    Optional<SignUpTable> findByLogin(LoginTable login);

    SignUpTable findByOtpAndEmail(Long otp, String email);
//    SignUpTable findByOtp(Long otp);
}

