package in.deloitte.screening.app.service;

import in.deloitte.screening.app.exceptions.UserSignupException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private SignUpRepository repository;
    @Mock
    private LoginRepository loginRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
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
    @Autowired
    UserRolesRepository rolesRepository;

    @Test
    @DisplayName("test user Signup success")
    public void SignupSuccessTest() {
        // When the createUser method is called, return the mock user
        LoginTable login = new LoginTable();
        login.setUserName(user.getEmail().split("@")[0]);
        login.setPassword(passwordEncoder.encode(user.getPassword()));

        UserRoles roles = rolesRepository.findByRoleCode("002").get();

        login.setRole(roles);
        login = loginRepository.save(login);

        // Create a SignUpTable entity
        SignUpTable signUpTable = new SignUpTable();
        signUpTable.setEmail(user.getEmail());
        signUpTable.setLogin(login);

        when(repository.save(signUpTable)).thenReturn(signUpTable);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
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
}
