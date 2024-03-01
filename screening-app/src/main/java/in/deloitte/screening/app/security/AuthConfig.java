package in.deloitte.screening.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.deloitte.screening.app.user.repositories.LoginRepository;

@Configuration
public class AuthConfig {
	
	private final LoginRepository loginRepository;
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		 UserDetails user1 = User.builder().username("SamrSawant").password(passwordEncoder().encode("abc")).roles("USER").build();
//		 UserDetails user2 = User.builder().username("snkalyankar").password(passwordEncoder().encode("abc")).roles("USER").build();
//		 UserDetails user3 = User.builder().username("shwlandge").password(passwordEncoder().encode("abc")).roles("USER").build();
//		 
//		 return new InMemoryUserDetailsManager(user1,user2,user3);
		
//	}
	
	public AuthConfig(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	@Bean
	public UserDetailsService getUserDetails() {
		return userName -> loginRepository.findByUserName(userName)
				.orElseThrow(() -> new RuntimeException("User Not Found with name : " + userName));
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
}
