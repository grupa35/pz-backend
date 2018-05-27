package pl.shopgen.models;

public class ErrorDTO {
    int code;
    String message;

    public ErrorDTO() {

    }

    public ErrorDTO(int code) {
        this.code = code;
    }

    public ErrorDTO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
