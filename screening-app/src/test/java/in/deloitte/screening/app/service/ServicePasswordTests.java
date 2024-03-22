package in.deloitte.screening.app.service;

import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.user.dto.ForgotPasswordRequest;
import in.deloitte.screening.app.user.entities.LoginTable;
import in.deloitte.screening.app.user.entities.SignUpTable;
import in.deloitte.screening.app.user.repositories.SignUpRepository;
import in.deloitte.screening.app.user.services.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServicePasswordTests {
    @Mock
    private SignUpRepository repository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("test for Update Password Success")
    public void testUpdatePasswordSuccessful() {

        String newPassword = "Shweta@1234";
        String currentPassword = "Shweta@123";
        String email = "shwlandge@deloitte.com";

        SignUpTable existingUser = new SignUpTable();
        existingUser.setEmail(email);
        existingUser.setLogin(new LoginTable("shwlandge", passwordEncoder.encode(currentPassword)));

        LoginTable login = new LoginTable();
        login.setUserName("shwlandge");
        login.setPassword(passwordEncoder.encode(currentPassword));

        when(repository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        when(repository.save(any(SignUpTable.class))).thenAnswer(invocation -> {
            SignUpTable savedUser = invocation.getArgument(0);

            savedUser.getLogin().setPassword(passwordEncoder.encode(newPassword));
            return savedUser;
        });

        when(passwordEncoder.encode(newPassword)).thenReturn("$2a$10$2QkHv.fGjMKWkeCq8Tua7.GkzCg2mRVF1Vd3v5hOCXcHrPwZQcH2i");

        SignUpTable updatedUser = userService.updatePassword(login.getPassword(), newPassword, existingUser.getEmail());

        verify(repository, times(1)).findByEmail(email);

        verify(repository, times(1)).save(any(SignUpTable.class));

        Assertions.assertEquals("$2a$10$2QkHv.fGjMKWkeCq8Tua7.GkzCg2mRVF1Vd3v5hOCXcHrPwZQcH2i", updatedUser.getLogin().getPassword());
    }


    @Test
    @DisplayName("test for Update Password Invalid Current Password")
    public void testUpdatePasswordInvalidCurrentPassword() {
        String newPassword = "Shweta@1234";
        String currentPassword = "Shweta@123";
        String email = "shwlandge@deloitte.com";
        String existingPassword = "Shweta1234";

        SignUpTable user = new SignUpTable();
        LoginTable login = new LoginTable();
        login.setPassword(existingPassword);
        user.setLogin(login);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(currentPassword, login.getPassword())).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(currentPassword, newPassword, email));

        verify(repository).findByEmail(email);
        verify(passwordEncoder).matches(currentPassword, login.getPassword());

        verifyNoMoreInteractions(repository, passwordEncoder);
    }

    @Test
    @DisplayName("test for Update Password User Not Found")
    public void testUpdatePasswordUserNotFound() {
        String newPassword = "Shweta@1234";
        String currentPassword = "Shweta@123";
        String email = "nonexistent@deloitte.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        verify(repository, never()).save(any(SignUpTable.class));

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updatePassword(currentPassword, newPassword, email));
    }

    @Test
    @DisplayName("test for generate OTP Success")
    public void generateOTPSuccessTest() {
        String email = "shwlandge@deloitte.com";

        SignUpTable user = new SignUpTable();
        user.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        Long otp = userService.generateOtp(email);

        verify(repository, times(1)).findByEmail(email);

        Assertions.assertNotNull(otp);
    }

    @Test
    @DisplayName("test for generate OTP Failed")
    public void generateOTPFailedTest() {
        String email = "nonexistent@example.com";

        // Mock the repository behavior to return an empty Optional, simulating user not found
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUser(email));
    }

    @Test
    @DisplayName("test for validate OTP Success")
    public void validateOTPExpirySuccessTest() {

        long validOTP = 123456;
        String email = "shwlandge@deloitte.com.com";

        SignUpTable sign = new SignUpTable();
        sign.setOtp(validOTP);
        sign.setOtpExpirationTime(new Date(System.currentTimeMillis() + 50000)); // future time

        when(repository.findByOtpAndEmail(validOTP, email)).thenReturn(Optional.of(sign));

        String result = userService.validateOTP(validOTP, email);

        Assertions.assertEquals("OTP Validated Successfully.", result);
    }

    @Test
    @DisplayName("test for validate OTP Failed due to invalid OTP")
    public void validateOTPExpiryInvalidOTPTest() {
        long invalidOTP = 999999;
        String email = "shwlandge@deloitte.com.com";

        when(repository.findByOtpAndEmail(invalidOTP, email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.validateOTP(invalidOTP, email));
    }

    @Test
    @DisplayName("test for validate OTP Failed due to invalid User")
    public void validateOTPExpiryInvalidUserTest() {
        long validOTP = 999999;
        String invalidEmail = "nonexistent@example.com";

        when(repository.findByOtpAndEmail(validOTP, invalidEmail)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.validateOTP(validOTP, invalidEmail));
    }

    @Test
    @DisplayName("test for validate OTP failed due to expired OTP")
    public void validateOTPExpiredOTPTest() {
        long expiredOTP = 123456;
        String email = "shwlandge@deloitte.com.com";

        SignUpTable user = new SignUpTable();
        user.setOtp(expiredOTP);
        user.setOtpExpirationTime(new Date(System.currentTimeMillis() - 50000));

        when(repository.findByOtpAndEmail(expiredOTP, email)).thenReturn(Optional.of(user));

        assertThrows(UserNotFoundException.class, () -> userService.validateOTP(expiredOTP, email));
    }

    @Test
    @DisplayName("test for save OTP for existing user")
    public void saveOtpForExistingUserTest() {
        String email = "shwlandge@deloitte.com.com";
        long otp = 123456;

        SignUpTable existingUser = new SignUpTable();
        existingUser.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        assertDoesNotThrow(() -> userService.saveOtp(email, otp));

        verify(repository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("test for save OTP for not existing user")
    public void saveOtpForExistingNonExistingUserTest() {
        String email = "nonexistinguser@example.com";
        long otp = 123456;

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.saveOtp(email, otp));

        verify(repository, never()).save(any(SignUpTable.class));
    }

    @Test
    @DisplayName("test for forgot password with valid OTP")
    public void forgotPasswordWithValidOTP() throws UserNotFoundException {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("shwlandge@deloitte.com");
        request.setOtp(123456L);
        request.setPassword("Shweta@123");

        SignUpTable existingUser = new SignUpTable();
        existingUser.setOtpExpirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        existingUser.setLogin(new LoginTable());

        when(repository.findByOtpAndEmail(request.getOtp(), request.getEmail())).thenReturn(Optional.of(existingUser));

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        SignUpTable updatedUser = userService.forgotPassword(request);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals("encodedPassword", updatedUser.getLogin().getPassword());
        Assertions.assertNull(updatedUser.getOtp());
        Assertions.assertNull(updatedUser.getOtpExpirationTime());
        verify(repository, times(1)).save(existingUser);
    }

    @Test
    @DisplayName("test for forgot password with inValid OTP")
    public void forgotPasswordWithInvalidOTP() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("shwlandge@deloitte.com");
        request.setOtp(123456L);
        request.setPassword("Shweta@123");

        when(repository.findByOtpAndEmail(request.getOtp(), request.getEmail())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.forgotPassword(request));
        verify(repository, never()).save(any(SignUpTable.class));
    }
}
