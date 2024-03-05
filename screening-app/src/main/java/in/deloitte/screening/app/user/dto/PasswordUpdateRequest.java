package in.deloitte.screening.app.user.dto;

public class PasswordUpdateRequest {
    private String email;
    private String currentPassword;
    private String newPassword;

    public String getEmail() {
        return email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
