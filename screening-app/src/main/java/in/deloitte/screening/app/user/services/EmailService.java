package in.deloitte.screening.app.user.services;

import in.deloitte.screening.app.user.dto.EmailDetailsDto;
import in.deloitte.screening.app.user.entities.SignUpTable;

public interface EmailService {
    void sendEmail(EmailDetailsDto details);

	EmailDetailsDto getEmailDetails(String email, SignUpTable user, Long otp);
}
