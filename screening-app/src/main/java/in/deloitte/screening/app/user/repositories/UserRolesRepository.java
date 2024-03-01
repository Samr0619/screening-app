package in.deloitte.screening.app.user.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import in.deloitte.screening.app.user.entities.UserRoles;

import java.util.Optional;

public interface UserRolesRepository extends JpaRepository<UserRoles,Long> {
    
    Optional<UserRoles> findByRoleCode(String roleCode);
}
