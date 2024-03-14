package in.deloitte.screening.app.user.dto;

public class JWTResponse {

    private String jwtToken;
    private String username;
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JWTResponse(String jwtToken, String username) {
        super();
        this.jwtToken = jwtToken;
        this.username = username;
    }

    public JWTResponse() {
        super();
    }

    @Override
    public String toString() {
        return "JWTResponse{" +
                "jwtToken='" + jwtToken + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
