package in.deloitte.screening.app.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.deloitte.screening.app.user.bean.MessageResponse;
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
		
		ErrorResponse msgres = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(),
				"Access Denied !! : "+authException.getMessage());
		
		
		String jsonResponse = new ObjectMapper().writeValueAsString(msgres);
		 
		PrintWriter pw = response.getWriter();
		pw.println(jsonResponse);
	}
	
	

}

class ErrorResponse implements Serializable{
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
