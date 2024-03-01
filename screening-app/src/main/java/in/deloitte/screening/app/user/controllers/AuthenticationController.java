package in.deloitte.screening.app.user.controllers;

import com.google.gson.Gson;

import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.user.bean.ForgotPasswordRequest;
import in.deloitte.screening.app.user.bean.JWTRequest;
import in.deloitte.screening.app.user.bean.JWTResponse;
import in.deloitte.screening.app.user.bean.MessageResponse;
import in.deloitte.screening.app.user.dto.EmailDetailsDto;
import in.deloitte.screening.app.user.dto.SignUpDto;
import in.deloitte.screening.app.user.entities.SignUpTable;
import in.deloitte.screening.app.user.services.EmailService;
import in.deloitte.screening.app.user.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private static final Gson gson = new Gson();
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService senderService;
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTRequest request) {
        JWTResponse response = null;
        logger.debug("API ::: /login");
        response = userService.validateLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto bean) {
        String status = null;
        logger.debug("API ::: /signUp");
        status = userService.SaveSignUp(bean);
        
        MessageResponse response = new MessageResponse();
        response.setStatus("Success");
        response.setMessage("User Sign Up Successfully");
        response.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/sentOTP")
    public ResponseEntity<?> sentOTPForForgotUserPassword(@RequestParam String email) {
        //generate OTP
        logger.debug("API ::: /sentOTP");
        Long otp = userService.generateOtp(email);
        System.out.println("Otp generated");

        // Save the OTP to the database
        userService.saveOtp(email, otp);
        System.out.println("Otp saved to database");

        // Email the user with the OTP
        SignUpTable user = userService.getUser(email);
        EmailDetailsDto details = getEmailDetails(email, user, otp);
        senderService.sendEmail(details);

        MessageResponse response = new MessageResponse();
        response.setStatus("Success");
        response.setMessage("Mail sent Successfully");
        response.setTimeStamp(LocalDateTime.now());


        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private static EmailDetailsDto getEmailDetails(String email, SignUpTable user, Long otp) {
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

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotUserPassword(@RequestBody ForgotPasswordRequest request) throws UserNotFoundException {
        return new ResponseEntity<>(userService.forgotPassword(request), HttpStatus.OK);
    }

    @GetMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestParam Long otp) throws UserNotFoundException {
        MessageResponse response = new MessageResponse();
        String res = userService.validateOTP(otp);
        response.setStatus("Success");
        response.setMessage(res);
        response.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
