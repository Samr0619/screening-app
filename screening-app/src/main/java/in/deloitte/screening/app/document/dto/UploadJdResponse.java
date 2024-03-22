package in.deloitte.screening.app.document.dto;

import org.springframework.http.HttpStatus;

public record UploadJdResponse(String message,String jdFileName,HttpStatus status) {
	
}
