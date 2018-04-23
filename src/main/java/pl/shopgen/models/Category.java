package pl.shopgen.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Category {
    @Id
    private String id;
    private String name;
    private List<Category> subcategories;

    protected Category() {
        subcategories = new ArrayList<>();
    }

    public Category(String name, List<Category> subcategories) {
        this.name = name;
        this.subcategories = subcategories;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }

        Category category = (Category) o;

        if(id != null ? !id.equals(category.id) : category.id != null) {
            return false;
        }
        if(name != null ? !name.equals(category.name) : category.name != null) {
            return false;
        }
        return subcategories.equals(category.subcategories);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + subcategories.hashCode();
        return result;

    }
}
