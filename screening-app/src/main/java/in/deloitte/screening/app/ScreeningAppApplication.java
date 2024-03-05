package in.deloitte.screening.app;

import javax.management.relation.Role;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import in.deloitte.screening.app.user.entities.RoleEnum;


@SpringBootApplication
public class ScreeningAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreeningAppApplication.class, args); 
		
		System.out.println(RoleEnum.ADMIN.getRole()+" ::  :: :: LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
	}

}
