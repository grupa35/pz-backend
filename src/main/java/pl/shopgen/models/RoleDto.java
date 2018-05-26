package pl.shopgen.models;


import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;

public class RoleDto implements SimpleDto {

    private int status;

    private String errorMessage;

    private String id;

    private String name;


    public RoleDto() {
    }

    public RoleDto(Role role) {
        if(role != null) {
            id = role.getId();
            name = role.getName();
        }
    }

    public static FieldDescriptor[] fieldsDescriptor() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("status").description("Status of the response"),
                PayloadDocumentation.fieldWithPath("errorMessage").type("String").description(
                        "Description of the error."),
                PayloadDocumentation.fieldWithPath("id").description("Id of the role"),
                PayloadDocumentation.fieldWithPath("name").description("Name of the role")
        };
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
