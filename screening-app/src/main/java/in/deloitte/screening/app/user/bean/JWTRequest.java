package in.deloitte.screening.app.user.bean;

public class JWTRequest {
	private String email;
	private String passString;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassString() {
		return passString;
	}
	public void setPassString(String passString) {
		this.passString = passString;
	}
	
	
}
