package in.deloitte.screening.app.exceptions;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import in.deloitte.screening.app.user.controllers.AuthenticationController;
import jakarta.servlet.http.HttpServletRequest;


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
		
		logger.error("Global Exception :: "+exceptionResponse.toString());
		
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
