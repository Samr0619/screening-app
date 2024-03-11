package in.deloitte.screening.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
public class ScreeningAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreeningAppApplication.class, args);
	}

}
