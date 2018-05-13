package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class Category extends SimpleObject {

    private String name;

    private List<Category> subcategories = new ArrayList<>();

    public Category(String name, List<Category> subcategories) {
        this.name = name;
        this.subcategories.addAll(subcategories);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + subcategories.hashCode();
        return result;
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

    public Category getSubCategoryById(String categoryId) {
        if(categoryId == null || subcategories.size() == 0) {
            return null;
        }

        Category foundedCategory = subcategories.stream()
                .filter(category -> Objects.equals(category.getId(), categoryId))
                .findFirst()
                .orElse(null);

        if(foundedCategory == null) {
            foundedCategory = subcategories.stream()
                    .map(category -> category.getSubCategoryById(categoryId))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        return foundedCategory;
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
