package in.deloitte.screening.app.repository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import in.deloitte.screening.app.user.entities.UserRoles;
import in.deloitte.screening.app.user.repositories.UserRolesRepository;


@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class UserRolesRepositoryTests {

    @Mock
    private UserRolesRepository userRolesRepository;

    @Before
    public void setUp() {
    	
        // Define mock behavior for findByRoleCode method when role exists
        UserRoles userRole = new UserRoles();
        userRole.setRoleCode("ROLE_ADMIN");
        when(userRolesRepository.findByRoleCode("ROLE_ADMIN")).thenReturn(Optional.of(userRole));

        // Define mock behavior for findByRoleCode method when role doesn't exist
        when(userRolesRepository.findByRoleCode("NON_EXISTING_ROLE")).thenReturn(Optional.empty());
    }

    @Test
    public void testFindByRoleCode_Success() {
        // Arrange
        String roleCode = "ROLE_ADMIN";

        // Act
        Optional<UserRoles> userRoleOptional = userRolesRepository.findByRoleCode(roleCode);

        // Assert
        assertEquals(true, userRoleOptional.isPresent());
        assertEquals(roleCode, userRoleOptional.get().getRoleCode());
    }

    @Test
    public void testFindByRoleCode_Failure() {
        // Arrange
        String roleCode = "NON_EXISTING_ROLE";

        // Act
        Optional<UserRoles> userRoleOptional = userRolesRepository.findByRoleCode(roleCode);

        // Assert
        assertEquals(false, userRoleOptional.isPresent());
    }

}
