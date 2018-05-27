package pl.shopgen.models;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;

public class PasswordResetDto {
    private boolean success;
    private String message;

    public PasswordResetDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static FieldDescriptor[] fieldsDescriptor() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("success").description("Status of the response"),
                PayloadDocumentation.fieldWithPath("message").type("String").description(
                        "Description of the message.")
        };
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
