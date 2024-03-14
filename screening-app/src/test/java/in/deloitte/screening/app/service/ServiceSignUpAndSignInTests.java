package in.deloitte.screening.app.service;

import in.deloitte.screening.app.exceptions.AuthorizationException;
import in.deloitte.screening.app.exceptions.UserNotFoundException;
import in.deloitte.screening.app.exceptions.UserSignupException;
import in.deloitte.screening.app.security.JWTHelper;
import in.deloitte.screening.app.user.dto.JWTRequest;
import in.deloitte.screening.app.user.dto.JWTResponse;
import in.deloitte.screening.app.user.dto.SignUpDto;
import in.deloitte.screening.app.user.entities.LoginTable;
import in.deloitte.screening.app.user.entities.SignUpTable;
import in.deloitte.screening.app.user.entities.UserRoles;
import in.deloitte.screening.app.user.repositories.LoginRepository;
import in.deloitte.screening.app.user.repositories.SignUpRepository;
import in.deloitte.screening.app.user.repositories.UserRolesRepository;
import in.deloitte.screening.app.user.services.UserServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceSignUpAndSignInTests {
    @Mock
    private SignUpRepository repository;
    @Mock
    private LoginRepository loginRepository;
    @Mock
    private UserRolesRepository rolesRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTHelper jwtHelper;
    @InjectMocks
    private UserServiceImpl userService;
    private SignUpDto user;

    @BeforeEach
    void setUp() {
        user = new SignUpDto("shwlandge@deloitte.com", "Shweta@123", "Shweta@123");
    }

    @AfterEach
    void tearDown() {
        user = null;
    }

    @Test
    @DisplayName("test user Signup success")
    public void SignupSuccessTest() {

        UserRoles mockRole = new UserRoles();
        mockRole.setRoleCode("002");
        mockRole.setRoleDesc("User");
        mockRole.setRoleShortName("User");
        mockRole.setRoleId(2L);

        when(rolesRepository.findByRoleCode("002")).thenReturn(Optional.of(mockRole));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        LoginTable login = new LoginTable();
        login.setUserName(user.getEmail().split("@")[0]);
        login.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<UserRoles> roleOp = rolesRepository.findByRoleCode("002");
        if (roleOp.isPresent()) {
            login.setRole(roleOp.get());
        }

        login = loginRepository.save(login);

        SignUpTable signUpTable = new SignUpTable();
        signUpTable.setEmail(user.getEmail());
        signUpTable.setLogin(login);

        when(repository.save(any(SignUpTable.class))).thenReturn(signUpTable);

        Assertions.assertEquals("SUCCESS", userService.SaveSignUp(user));
    }

    @Test
    @DisplayName("test user Signup failure for Already Exist User")
    void SignupFailedDueToExistingUserTest() {
        SignUpTable signUpTable = new SignUpTable();
        signUpTable.setEmail(user.getEmail());
        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(signUpTable));
        Assertions.assertThrows(UserSignupException.class, () -> userService.SaveSignUp(user));
    }

    @Test
    @DisplayName("test user SignIn Success")
    public void SignInSuccessTest() {
        JWTRequest request = new JWTRequest();
        // Prepare test data
        request.setEmail("shwlandge@deloitte.com");
        request.setPassString("Shweta@123");

        SignUpTable signUpTable = new SignUpTable();
        signUpTable.setEmail("shwlandge@deloitte.com");
        signUpTable.setLogin(new LoginTable("shwlandge", "Shweta@123"));

        LoginTable login = new LoginTable();
        login.setUserName("shwlandge");
        login.setPassword("Shweta@123");

        Authentication successfulAuthentication = new UsernamePasswordAuthenticationToken("shwlandge", "Shweta@123");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("shwlandge", "Shweta@123")))
                .thenReturn(successfulAuthentication);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("shwlandge")
                .password(request.getPassString())
                .roles("User")
                .build();
        when(userDetailsService.loadUserByUsername("shwlandge")).thenReturn(userDetails);

        String expectedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzYW1yc2F3YW50MyIsImlhdCI6MTcxMDMwODc4MywiZXhwIjoxNzEwMzEyMzgzfQ.bbCH3QIOGuGL5VBv67LjCEJFpeGArLERsMNGMxfUpuE";
        when(jwtHelper.generateToken(userDetails)).thenReturn(expectedToken);

        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.of(signUpTable));

        when(loginRepository.findByUserName(login.getUserName())).thenReturn(Optional.of(login));

        when(repository.findByLogin(login)).thenReturn(Optional.of(signUpTable));

        JWTResponse response = userService.validateLogin(request);

        Assertions.assertEquals(signUpTable.getEmail(), response.getEmail());
        Assertions.assertEquals(login.getUsername(), response.getUsername());
    }

    @Test
    @DisplayName("test user SignIn failure for User not found")
    void SignInFailedDueToNotFoundUserTest() {
        JWTRequest request = new JWTRequest("shwlandge@deloitte.com", "Shweta@123");
        when(repository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Assertions.assertThrows(AuthorizationException.class, () -> userService.validateLogin(request));
    }

    @Test
    @DisplayName("test for get User Success")
    void getUserSuccessTest() {
        String email = "shwlandge@deloitte.com";
        SignUpTable user = new SignUpTable();
        user.setEmail(email);

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        SignUpTable retrievedUser = userService.getUser(email);

        verify(repository, times(1)).findByEmail(email);

        Assertions.assertNotNull(retrievedUser);
        Assertions.assertEquals(email, retrievedUser.getEmail());
    }

    @Test
    @DisplayName("test for get User Failed")
    void getUserFailedTest() {
        String email = "nonexistent@example.com";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUser(email));
    }
}
