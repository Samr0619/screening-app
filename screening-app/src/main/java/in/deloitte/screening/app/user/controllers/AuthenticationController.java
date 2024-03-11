package in.deloitte.screening.app.user.controllers;

import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.user.dto.*;
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
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService senderService;
    private Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JWTRequest request) {

        JWTResponse response = null;
        response = userService.validateLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto bean) {

        String status = null;
        status = userService.SaveSignUp(bean);

        MessageResponse response = new MessageResponse();
        response.setStatus("Success");
        response.setMessage("User Sign Up Successfully");
        response.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @param email
     * @return
     */
    @PutMapping("/send-otp")
    public ResponseEntity<?> sentOTPForForgotUserPassword(@RequestParam String email) {

        Long otp = userService.generateOtp(email);
        userService.saveOtp(email, otp);
        SignUpTable user = userService.getUser(email);
        EmailDetailsDto details = senderService.getEmailDetails(email, user, otp);
        senderService.sendEmail(details);

        MessageResponse response = new MessageResponse();
        response.setStatus("Success");
        response.setMessage("Mail sent Successfully");
        response.setTimeStamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotUserPassword(@RequestBody ForgotPasswordRequest request) throws UserNotFoundException {
        return new ResponseEntity<>(userService.forgotPassword(request), HttpStatus.OK);
    }

    @GetMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestParam Long otp, @RequestParam String email) throws UserNotFoundException {

        MessageResponse response = new MessageResponse();
        String res = userService.validateOTP(otp, email);
        response.setStatus("Success");
        response.setMessage(res);
        response.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
