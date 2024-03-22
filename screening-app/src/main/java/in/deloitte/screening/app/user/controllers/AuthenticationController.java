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

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * @param req
     * @return
     */
    @PutMapping("/send-otp")
    public ResponseEntity<MessageResponse> sentOTPForForgotUserPassword(@RequestBody SendOtpRequestDto req) {

        Long otp = userService.generateOtp(req.email());
        userService.saveOtp(req.email(), otp);
        SignUpTable user = userService.getUser(req.email());
        EmailDetailsDto details = senderService.getEmailDetails(req.email(), user, otp);
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

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestBody ValidateOtpRequestDto req) throws UserNotFoundException {

        MessageResponse response = new MessageResponse();
        String res = userService.validateOTP(req.otp(), req.email());
        response.setStatus("Success");
        response.setMessage(res);
        response.setTimeStamp(LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
