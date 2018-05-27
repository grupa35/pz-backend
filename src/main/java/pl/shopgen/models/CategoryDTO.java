package pl.shopgen.models;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;

import java.util.List;

public class CategoryDTO implements SimpleDto {

    private int status;

    private String errorMessage;

    private String id;

    private String name;

    private List<Category> subcategories;

    public CategoryDTO() {

    }

    public CategoryDTO(Category category) {
        if(category != null) {
            id = category.getId();
            name = category.getName();
            subcategories = category.getSubcategories();
        }
    }

    public static FieldDescriptor[] fieldsDescriptor() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("status").description("Status of the response"),
                PayloadDocumentation.fieldWithPath("errorMessage").type("String").description(
                        "Description of the error."),
                PayloadDocumentation.fieldWithPath("id").description("Id of the category"),
                PayloadDocumentation.fieldWithPath("name").description("Name of the category"),
                PayloadDocumentation.fieldWithPath("subcategorires").description("List of suncategories")
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

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
