package in.deloitte.screening.app.user.services;

import in.deloitte.screening.app.exceptions.AuthorizationException;
import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.exceptions.UserSignupException;
import in.deloitte.screening.app.user.bean.ForgotPasswordRequest;
import in.deloitte.screening.app.user.bean.JWTRequest;
import in.deloitte.screening.app.user.bean.JWTResponse;
import in.deloitte.screening.app.user.dto.SignUpDto;
import in.deloitte.screening.app.user.entities.SignUpTable;

public interface UserService {

    SignUpTable updatePassword(String currentPassword, String newPassword, String email) throws UserNotFoundException;

    String SaveSignUp(SignUpDto bean) throws UserSignupException;

    JWTResponse validateLogin(JWTRequest request) throws AuthorizationException;
    SignUpTable getUser(String email) throws UserNotFoundException;

    Long generateOtp(String email) throws UserNotFoundException;

    void saveOtp(String email, Long otp) throws UserNotFoundException;

    SignUpTable forgotPassword(ForgotPasswordRequest request) throws UserNotFoundException;
    String validateOTP(Long otp) throws UserNotFoundException;
}
