package pl.shopgen.models.test;

public class SimpleTestMessage {
    private String message;

    public SimpleTestMessage(String message) {
        this.message = message;
    }

    public SimpleTestMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String mes) {
        this.message = mes;
    }
}
