package in.deloitte.screening.app.user.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import in.deloitte.screening.app.user.dto.EmailDetailsDto;
import in.deloitte.screening.app.user.entities.SignUpTable;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    
    private Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    public void sendEmail(EmailDetailsDto details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
        } catch (Exception ignored) {
        	logger.error("Error Occured while sending Mail : {} ",ignored.getMessage());
        }
    }
    
    @Override
    public EmailDetailsDto getEmailDetails(String email, SignUpTable user, Long otp) {
        String content = "Dear " + user.getLogin().getUsername() + ", \n\n"
                + "For security reason, you're required to use the following "
                + "One Time Password to login: "
                + otp
                + "\nNote: this OTP is set to expire in 5 minutes."
                + "\n\nRegards"
                + "\nAdmin Team";

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        EmailDetailsDto details = new EmailDetailsDto();
        details.setRecipient(email);
        details.setMsgBody(content);
        details.setSubject(subject);
        return details;
    }
}
