package in.deloitte.screening.app.controller;

import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.user.controllers.AuthenticationController;
import in.deloitte.screening.app.user.controllers.UserController;
import in.deloitte.screening.app.user.dto.*;
import in.deloitte.screening.app.user.entities.LoginTable;
import in.deloitte.screening.app.user.entities.SignUpTable;
import in.deloitte.screening.app.user.services.EmailServiceImpl;
import in.deloitte.screening.app.user.services.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ControllerPasswordTests {
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private AuthenticationController authController;
    @Mock
    private EmailServiceImpl senderService;

    @InjectMocks
    private UserController userController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @AfterEach
    public void tearDown() {
        mockMvc = null;
    }

    @Test
    @DisplayName("test for update password success")
    public void updatePasswordSuccessTest() {

        String currentPassword = "Shweta@123";
        String newPassword = "Shweta@1234";
        String email = "shwlandge@deloitte.com";

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        request.setEmail(email);

        SignUpTable updatedUser = new SignUpTable();

        when(userService.updatePassword(currentPassword, newPassword, email)).thenReturn(updatedUser);

        ResponseEntity<?> responseEntity = userController.updatePassword(request);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(updatedUser, responseEntity.getBody());

        verify(userService, times(1)).updatePassword(currentPassword, newPassword, email);
    }

    @Test
    @DisplayName("test for update password incorrect current password")
    public void updatePasswordIncorrectCurrentPasswordTest() {

        String currentPassword = "Shweta@123";
        String newPassword = "Shweta@1234";
        String email = "shwlandge@deloitte.com";
        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        request.setEmail(email);

        when(userService.updatePassword(currentPassword, newPassword, email))
                .thenThrow(UserNotFoundException.class);

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.updatePassword(request));

        verify(userService, times(1)).updatePassword(currentPassword, newPassword, email);
    }

    @Test
    @DisplayName("test for update password non existing user")
    public void updatePasswordForNonExistingUserTest() {
        String currentPassword = "Shweta@123";
        String newPassword = "Shweta@1234";
        String email = "shwlandge@deloitte.com";

        PasswordUpdateRequest request = new PasswordUpdateRequest();
        request.setCurrentPassword(currentPassword);
        request.setNewPassword(newPassword);
        request.setEmail(email);

        when(userService.updatePassword(currentPassword, newPassword, email))
                .thenThrow(new UserNotFoundException("User with this email not Present in Database"));

        Assertions.assertThrows(UserNotFoundException.class, () -> userController.updatePassword(request));

        verify(userService, times(1)).updatePassword(currentPassword, newPassword, email);
    }

    @Test
    @DisplayName("OTP send to user email success")
    public void sentOTPForForgotUserPasswordSuccessTest() {

        String email = "shwlandge@deloitte.com";
        SendOtpRequestDto requestDto = new SendOtpRequestDto(email);
        Long otp = 123456L;

        SignUpTable user = new SignUpTable();
        when(userService.generateOtp(email)).thenReturn(otp);
        when(userService.getUser(email)).thenReturn(user);

        EmailDetailsDto details = new EmailDetailsDto();
        when(senderService.getEmailDetails(email, user, otp)).thenReturn(details);

        ResponseEntity<?> responseEntity = authController.sentOTPForForgotUserPassword(requestDto);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        MessageResponse responseBody = (MessageResponse) responseEntity.getBody();
        assert responseBody != null;
        Assertions.assertEquals("Success", responseBody.getStatus());
        Assertions.assertEquals("Mail sent Successfully", responseBody.getMessage());

        Assertions.assertNotNull(responseBody.getTimeStamp());

        verify(userService).generateOtp(email);
        verify(userService).getUser(email);

        verify(senderService).getEmailDetails(email, user, otp);
        verify(senderService).sendEmail(details);
    }

    @Test
    @DisplayName("OTP send to user email failed")
    public void sentOTPForForgotUserPasswordFailedTest() {

        String email = "nonexistent@deloitte.com";
        SendOtpRequestDto requestDto = new SendOtpRequestDto(email);

        when(userService.generateOtp(email)).thenThrow(new UserNotFoundException("User with this email not Present in Database"));

        Assertions.assertThrows(UserNotFoundException.class, () -> authController.sentOTPForForgotUserPassword(requestDto));

        verify(userService).generateOtp(email);

        verifyNoInteractions(senderService);
    }

    @Test
    @DisplayName("test for forgot password success")
    public void forgotUserPasswordSuccessTest() {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("shwlandge@deloitte.com");
        request.setOtp(123456L);
        request.setPassword("Shweta@123");

        SignUpTable signUp = new SignUpTable();
        signUp.setEmail(request.getEmail());
        signUp.setLogin(new LoginTable("shwlandge", "Shweta@123"));

        ResponseEntity<?> expectedResponse = new ResponseEntity<>(signUp, HttpStatus.OK);

        when(userService.forgotPassword(request)).thenReturn(signUp);

        ResponseEntity<?> actualResponseEntity = authController.forgotUserPassword(request);

        Assertions.assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
        Assertions.assertEquals(expectedResponse.getBody(), actualResponseEntity.getBody());

        verify(userService).forgotPassword(request);
    }

    @Test
    @DisplayName("test for forgot password failed")
    public void forgotUserPasswordFailedTest() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("shwlandge@deloitte.com");
        request.setOtp(123456L);
        request.setPassword("Shweta@123");

        when(userService.forgotPassword(request)).thenThrow(new UserNotFoundException("Invalid OTP"));

        Assertions.assertThrows(UserNotFoundException.class, () -> authController.forgotUserPassword(request));

        verify(userService).forgotPassword(request);
    }

    @Test
    @DisplayName("test for validate otp Success")
    public void validateOtpSuccessTest() {

        Long otp = 123456L;
        String email = "shwlandge@deloitte.com";
        ValidateOtpRequestDto requestDto = new ValidateOtpRequestDto(otp, email);

        String successMessage = "OTP validated successfully";
        when(userService.validateOTP(otp, email)).thenReturn(successMessage);

        ResponseEntity<?> responseEntity = authController.validateOtp(requestDto);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        MessageResponse responseBody = (MessageResponse) responseEntity.getBody();
        assert responseBody != null;
        Assertions.assertEquals("Success", responseBody.getStatus());
        Assertions.assertEquals(successMessage, responseBody.getMessage());

        Assertions.assertNotNull(responseBody.getTimeStamp());

        verify(userService).validateOTP(otp, email);
    }

    @Test
    @DisplayName("test for validate otp failed due to incorrect otp or non-existing user")
    public void validateOtpFailedDueToInvalidOtpOrNonExistingUserTest() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("shwlandge@deloitte.com");
        request.setOtp(123456L);
        request.setPassword("Shweta@123");

        when(userService.forgotPassword(request)).thenThrow(new UserNotFoundException("Invalid OTP or User with this email is not Present in Database."));

        Assertions.assertThrows(UserNotFoundException.class, () -> authController.forgotUserPassword(request));

        verify(userService).forgotPassword(request);
    }

    @Test
    @DisplayName("test for validate otp failed expired OTP")
    public void validateOtpFailedDueToExpiredOtpTest() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("shwlandge@deloitte.com");
        request.setOtp(123456L);
        request.setPassword("Shweta@123");

        when(userService.forgotPassword(request)).thenThrow(new UserNotFoundException("Your OTP is expired. Create a new OTP if you want to Proceed."));

        Assertions.assertThrows(UserNotFoundException.class, () -> authController.forgotUserPassword(request));

        verify(userService).forgotPassword(request);
    }
}
