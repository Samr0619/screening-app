package in.deloitte.screening.app.user.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.deloitte.screening.app.user.entities.LoginTable;

public interface LoginRepository extends JpaRepository<LoginTable, Long>{
	
	public Optional<LoginTable> findByUserName(String userName);

	
}
