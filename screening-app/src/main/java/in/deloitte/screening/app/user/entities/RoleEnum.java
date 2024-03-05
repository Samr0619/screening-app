package in.deloitte.screening.app.user.entities;

public enum RoleEnum {
	USER("User"),
	ADMIN("Admin");
	
	private String role;
	
	RoleEnum(String string) {
		// TODO Auto-generated constructor stub
		this.role = string;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
}
