package in.deloitte.screening.app.user.services;

import in.deloitte.screening.app.exceptions.AuthorizationException;
import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.exceptions.UserSignupException;
import in.deloitte.screening.app.security.JWTHelper;
import in.deloitte.screening.app.user.dto.ForgotPasswordRequest;
import in.deloitte.screening.app.user.dto.JWTRequest;
import in.deloitte.screening.app.user.dto.JWTResponse;
import in.deloitte.screening.app.user.dto.SignUpDto;
import in.deloitte.screening.app.user.entities.LoginTable;
import in.deloitte.screening.app.user.entities.SignUpTable;
import in.deloitte.screening.app.user.entities.UserRoles;
import in.deloitte.screening.app.user.repositories.LoginRepository;
import in.deloitte.screening.app.user.repositories.SignUpRepository;
import in.deloitte.screening.app.user.repositories.UserRolesRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserRolesRepository userRolesRepository;

    @Autowired
    private SignUpRepository signUpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public SignUpTable updatePassword(String currentPassword, String newPassword, String email)
            throws UserNotFoundException {

        Optional<SignUpTable> user = signUpRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User with this email not Present in Database");
        }
        SignUpTable existingUser = user.get();
        LoginTable login = existingUser.getLogin();

        boolean isCurrentPasswordAndExistingPasswordMatches = passwordEncoder.matches(currentPassword,
                login.getPassword());
        if (login.getPassword() != null) {
            if (!isCurrentPasswordAndExistingPasswordMatches) {
                throw new UserNotFoundException("Password does not match");
            }
            login.setPassword(newPassword);
            login.setPassword(passwordEncoder.encode(newPassword));
            existingUser.setLogin(login);
        }
        return signUpRepository.save(existingUser);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String SaveSignUp(SignUpDto bean) throws UserSignupException {

        if (!bean.getPassword().equals(bean.getConfirmPassword())) {
            throw new UserSignupException("Passwords do not match please chcek....");
        }

        if (signUpRepository.findByEmail(bean.getEmail()).orElse(null) != null) {
            throw new UserSignupException("User Already Registered!");
        }

        if (bean.getPassword().length() < 8) {
            throw new UserSignupException("Password must be at least 8 characters long.");
        }
        if (!bean.getEmail().endsWith("@deloitte.com")) {
            throw new UserSignupException("Email must belong to deloitte.com domain.");
        }

        LoginTable login = new LoginTable();
        login.setUserName(bean.getEmail().split("@")[0]);
        login.setPassword(passwordEncoder.encode(bean.getPassword()));
        UserRoles roles = userRolesRepository.findByRoleCode("002").get();
        login.setRole(roles);

        login = loginRepository.save(login);

        // Create a SignUpTable entity
        SignUpTable signUpTable = new SignUpTable();
        signUpTable.setEmail(bean.getEmail());
        signUpTable.setLogin(login);

        signUpRepository.save(signUpTable);

        return "SUCCESS";
    }

    public JWTResponse validateLogin(JWTRequest request) throws AuthorizationException {

        JWTResponse response = null;
        try {
            SignUpTable signup = signUpRepository.findByEmail(request.getEmail()).orElse(null);
            if (signup == null) {
                throw new AuthorizationException("Credentials Not Valid!");
            }
            this.doAuthenticate(signup.getLogin().getUsername(), request.getPassString());
            UserDetails userDetails = userDetailsService.loadUserByUsername(signup.getLogin().getUsername());
            String token = this.jwtHelper.generateToken(userDetails);
            response = new JWTResponse(token, userDetails.getUsername());
            response.setEmail(signUpRepository
                    .findByLogin(loginRepository.findByUserName(userDetails.getUsername()).get()).get().getEmail());
            return response;
        } catch (BadCredentialsException e) {
            // TODO: handle exception
            throw new AuthorizationException("Credentials Not Valid!");
        }

    }

    @Override
    public SignUpTable getUser(String email) throws UserNotFoundException {

        Optional<SignUpTable> byEmail = signUpRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("User not Present in Database");
        }
        return byEmail.get();
    }

    private Authentication doAuthenticate(String userName, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, password);
        return authenticationManager.authenticate(authentication);
    }

    @Override
    public Long generateOtp(String email) throws UserNotFoundException {

        if (Optional.ofNullable(signUpRepository.findByEmail(email)).isEmpty())
            throw new UserNotFoundException("User with this email not Present in Database");
        Random random = new Random();
        return (long) (random.nextInt(900000) + 100000);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveOtp(String email, Long otp) throws UserNotFoundException {

        Optional<SignUpTable> userOptional = signUpRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with this email not Present in Database");
        }

        SignUpTable user = userOptional.get();
        user.setOtp(otp);
        user.setOtpExpirationTime(new Date(System.currentTimeMillis() + 5 * 60 * 1000));
        signUpRepository.save(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public SignUpTable forgotPassword(ForgotPasswordRequest request) throws UserNotFoundException {

        Optional<SignUpTable> userOptional = signUpRepository.findByOtpAndEmail(request.getOtp(), 
        		request.getEmail());
 
//        if (userOptional.isEmpty()) {
//            throw new UserNotFoundException("Invalid OTP");
//        }
        
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Invalid OTP or User with this email is not Present in Database.");
        }
       
        SignUpTable user = userOptional.get();

        if (user.getOtpExpirationTime().before(new Date())) {
            user.setOtp(null);
            throw new UserNotFoundException("Your OTP is expired. Create a new OTP if you want to Proceed.");
        }

        LoginTable loginTable = user.getLogin();
        loginTable.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setLogin(loginTable);
        user.setOtp(null);
        user.setOtpExpirationTime(null);
        signUpRepository.save(user);

        return user;
    }

    @Override
    public String validateOTP(Long otp, String email) throws UserNotFoundException {

        Optional<SignUpTable> userOp = signUpRepository.findByOtpAndEmail(otp, email);

        if (userOp.isEmpty()) {
            throw new UserNotFoundException("Invalid OTP or User with this email is not Present in Database.");
        }
        SignUpTable user = userOp.get();

        if (user.getOtpExpirationTime().before(new Date())) {
            user.setOtp(null);
            user.setOtpExpirationTime(null);
            signUpRepository.save(user);
            throw new UserNotFoundException("Your OTP is expired. Create a new OTP if you want to Proceed.");
        }
        
       
        return "OTP Validated Successfully.";
    }
}