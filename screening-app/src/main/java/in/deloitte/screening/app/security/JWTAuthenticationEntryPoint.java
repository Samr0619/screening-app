package in.deloitte.screening.app.security;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// TODO Auto-generated method stub
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		//PrintWriter pw = response.getWriter();
		//pw.println("Access Denied !! : "+authException.getMessage());
		
		 ErrorResponse errorResponse = new ErrorResponse("Access Denied !! : "+authException.getMessage(), authException.getMessage());
	        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);

	        PrintWriter writer = response.getWriter();
	        writer.println(jsonResponse);
	}
	
	  private static class ErrorResponse {
	        private String error;
	        private String message;

	        public ErrorResponse(String error, String message) {
	            this.error = error;
	            this.message = message;
	        }

			public String getError() {
				return error;
			}

			public void setError(String error) {
				this.error = error;
			}

			public String getMessage() {
				return message;
			}

			public void setMessage(String message) {
				this.message = message;
			}

	        
	    }

}
