package in.deloitte.screening.app.user.services;

import in.deloitte.screening.app.user.dto.EmailDetailsDto;

public interface EmailService {
    void sendEmail(EmailDetailsDto details);
}
