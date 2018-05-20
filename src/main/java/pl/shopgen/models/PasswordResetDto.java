package pl.shopgen.models;

public class PasswordResetDto {
    private boolean success;
    private String message;

    public PasswordResetDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public PasswordResetDto() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
