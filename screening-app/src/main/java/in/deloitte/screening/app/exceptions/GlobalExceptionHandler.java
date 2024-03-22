package in.deloitte.screening.app.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;


@ControllerAdvice
public class GlobalExceptionHandler {
	
	  private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(
			HttpMessageNotReadableException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		exceptionResponse.setError(HttpStatus.BAD_REQUEST.name());
		exceptionResponse.setDefaultMessage(exception.getMessage());
		exceptionResponse.setFriendlyMessage("Please Remove invalid charecters from input");
		exceptionResponse.setExceptionType(exception.getClass().getCanonicalName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		
		logger.error("Global Exception :: " + exceptionResponse.toString());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ExceptionResponse> handleIOException(
			IOException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		exceptionResponse.setError(HttpStatus.BAD_REQUEST.name());
		exceptionResponse.setDefaultMessage(exception.getMessage());
		exceptionResponse.setFriendlyMessage("Please check input files...");
		exceptionResponse.setExceptionType(exception.getClass().getCanonicalName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Global Exception :: "+exceptionResponse.toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(DataNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleDataNotFoundException(
			DataNotFoundException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.NO_CONTENT.value());
		exceptionResponse.setError(HttpStatus.NO_CONTENT.name());
		exceptionResponse.setDefaultMessage(exception.getMessage());
		exceptionResponse.setFriendlyMessage("No Data Found for given inputs...");
		exceptionResponse.setExceptionType(exception.getClass().getCanonicalName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Global Exception :: "+exceptionResponse.toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(BadInputException.class)
	public ResponseEntity<ExceptionResponse> handleBadInputException(
			BadInputException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		exceptionResponse.setError(HttpStatus.BAD_REQUEST.name());
		exceptionResponse.setDefaultMessage(exception.getMessage());
		exceptionResponse.setFriendlyMessage("Some inputs are missing or invalid...");
		exceptionResponse.setExceptionType(exception.getClass().getCanonicalName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Global Exception :: "+exceptionResponse.toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(AuthorizationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ExceptionResponse> handleAuthException(
			AuthorizationException authorizationException, final HttpServletRequest request) {
		
		ExceptionResponse errorResponse = new ExceptionResponse();
		
		String statusMessage = HttpStatus.UNAUTHORIZED.toString();
		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setExceptionType(authorizationException.getClass().getName());
		errorResponse.setFriendlyMessage("Authentication Failed");
		errorResponse.setDefaultMessage(statusMessage.substring(statusMessage.indexOf(" ") + 1));
		errorResponse.setPath(request.getRequestURI());
		errorResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Authentication Failed", authorizationException);
	//	logger.debug("Authentication Failed", authorizationException); 
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
	
	@ExceptionHandler(UserSignupException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleSignupException(
	        UserSignupException signupException, final HttpServletRequest request) {
	    
	    ExceptionResponse errorResponse = new ExceptionResponse();
	    String statusMessage = HttpStatus.BAD_REQUEST.toString();
	    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
	    errorResponse.setExceptionType(signupException.getClass().getName());
	    errorResponse.setFriendlyMessage(statusMessage.substring(statusMessage.indexOf(" ") + 1));
	    errorResponse.setDefaultMessage(signupException.getMessage());
	    errorResponse.setPath(request.getRequestURI());
	    errorResponse.setTimeStamp(LocalDateTime.now());
	    logger.error(statusMessage.substring(statusMessage.indexOf(" ") + 1), signupException);
	  //  logger.debug(statusMessage.substring(statusMessage.indexOf(" ") + 1), signupException);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleUserNotFoundException(
			UserNotFoundException userNotFoundException, final HttpServletRequest request) {

		ExceptionResponse errorResponse = new ExceptionResponse();
		String statusMessage = HttpStatus.BAD_REQUEST.toString();
		errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		errorResponse.setExceptionType(userNotFoundException.getClass().getName());
		errorResponse.setFriendlyMessage(statusMessage.substring(statusMessage.indexOf(" ") + 1));
		errorResponse.setDefaultMessage(userNotFoundException.getMessage());
		errorResponse.setPath(request.getRequestURI());
		errorResponse.setTimeStamp(LocalDateTime.now());
		logger.error(statusMessage.substring(statusMessage.indexOf(" ") + 1), userNotFoundException);
		//  logger.debug(statusMessage.substring(statusMessage.indexOf(" ") + 1), userNotFoundException);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(
			DataIntegrityViolationException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		String defaultMessage = exception.getMessage().substring(
												exception.getMessage().indexOf("(email)"), 
												exception.getMessage().indexOf("]"));
		exceptionResponse.setDefaultMessage(defaultMessage);
		exceptionResponse.setFriendlyMessage("Invalid input...");
		exceptionResponse.setExceptionType(exception.getClass().getName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Global Exception :: " + exceptionResponse.toString());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(MissingServletRequestPartException.class)
	public ResponseEntity<ExceptionResponse> handleMissingServletRequestPartException(
			MissingServletRequestPartException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.BAD_REQUEST.value());
		exceptionResponse.setDefaultMessage(exception.getMessage());
		exceptionResponse.setFriendlyMessage("Something is broken...");
		exceptionResponse.setExceptionType(exception.getClass().getName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Global Exception :: "+exceptionResponse.toString());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionResponse> handleRuntimeException(
			RuntimeException exception, final HttpServletRequest request){
		
		exception.printStackTrace();
		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		exceptionResponse.setDefaultMessage(exception.getMessage());
		exceptionResponse.setFriendlyMessage("Something is broken...");
		exceptionResponse.setExceptionType(exception.getClass().getName());
		exceptionResponse.setPath(request.getRequestURI());
		exceptionResponse.setTimeStamp(LocalDateTime.now());
		logger.error("Global Exception :: "+exceptionResponse.toString());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
	}
}
