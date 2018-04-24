package pl.shopgen.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class Category extends SimpleObject {

    private String name;
    private List<Category> subcategories;

    protected Category() {
        subcategories = new ArrayList<>();
    }

    public Category(String name, List<Category> subcategories) {
        this.name = name;
        this.subcategories = subcategories;
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
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Category)) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }

        Category category = (Category) o;

        if(name != null ? !name.equals(category.name) : category.name != null) {
            return false;
        }
        return subcategories.equals(category.subcategories);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + subcategories.hashCode();
        return result;
    }
}
