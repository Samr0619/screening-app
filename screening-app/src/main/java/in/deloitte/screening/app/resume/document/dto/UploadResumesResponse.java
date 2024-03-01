package in.deloitte.screening.app.resume.document.dto;

import java.util.List;

public class UploadResumesResponse {

	private String message;
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	public UploadResumesResponse() {
		
	}

	public UploadResumesResponse(String message, List<String> failedToUpload) {
		super();
		this.message = message;
		this.failedToUpload = failedToUpload;
	}

	@Override
	public String toString() {
		return "UploadReumesResponse [message=" + message + ", failedToUpload=" + failedToUpload + "]";
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the failedToUpload
	 */
	public List<String> getFailedToUpload() {
		return failedToUpload;
	}

	/**
	 * @param failedToUpload the failedToUpload to set
	 */
	public void setFailedToUpload(List<String> failedToUpload) {
		this.failedToUpload = failedToUpload;
	}

	private List<String> failedToUpload;
}
