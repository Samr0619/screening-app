package in.deloitte.screening.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Value("${allowed.origin}")
		private String ALLOWED_ORIGIN;

	@Autowired
	private AuthenticationEntryPoint authEntrypoint;
	@Autowired
	private JwtAuthenticationFilter filter;

	@Bean 
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**",
						 "/swagger-ui/**",
						 "/v3/**",
						 "/actuator/**"
						 ).permitAll()
						.requestMatchers("/user/**",
								"/search/**",
								"/resume/**",
								 "/skills/**",
								"/jd/**",
								"/upload/**",
								 "/requisition/**"
								)
						.hasAnyAuthority("User", "Admin").anyRequest().authenticated())
				.exceptionHandling(excpt -> excpt.authenticationEntryPoint(authEntrypoint))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		 CorsConfiguration configuration = new CorsConfiguration();
		    configuration.addAllowedOrigin(ALLOWED_ORIGIN); // Allow requests from any origin (you can specify specific origins)
		    configuration.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
		    configuration.addAllowedHeader("*"); // Allow all headers
		    configuration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)
		    
		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", configuration);
		    return source;
	}

}
