package in.deloitte.screening.app.user.dto;

public class JWTRequest {
    private String email;
    private String passString;

    public JWTRequest(String email, String passString) {
        this.email = email;
        this.passString = passString;
    }

    public JWTRequest() {
    }

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
