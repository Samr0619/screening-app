package in.deloitte.screening.app.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import in.deloitte.screening.app.user.entities.LoginTable;
import in.deloitte.screening.app.user.repositories.LoginRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class LoginRepositoryTests {

    @Mock
    private LoginRepository loginRepository;
    


    @BeforeEach
    public void setUp() {
        // Define mock behavior for findByUserName method
        LoginTable user = new LoginTable();
        user.setUserName("samrdh");
        when(loginRepository.findByUserName("samrdh")).thenReturn(Optional.of(user));
        when(loginRepository.findByUserName("nonExistingUser")).thenReturn(Optional.empty());
    }

    @Test
    public void testFindByUserName() {
        // Arrange
        String userName = "samrdh";

        // Act
        Optional<LoginTable> userOptional = loginRepository.findByUserName(userName);

        // Assert
        assertEquals(true, userOptional.isPresent());
        
        assertEquals(userName, userOptional.get().getUserName());
    }


    @Test
    public void testFindByUserName_UserNotFound() {
        // Arrange
        String userName = "samrdh";

        // Act
        Optional<LoginTable> userOptional = loginRepository.findByUserName(userName);

        // Assert
        assertEquals(false, userOptional.isPresent());
    }
}
