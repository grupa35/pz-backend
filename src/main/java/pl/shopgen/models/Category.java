package pl.shopgen.models;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document
public class Category implements SimpleObject {

    private String id;

    private String name;

    private List<Category> subcategories = new ArrayList<>();

    public Category(String name, List<Category> subcategories) {
        this.name = name;
        this.subcategories.addAll(subcategories);
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

    @Override
    final public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (subcategories != null ? subcategories.hashCode() : 0);
        return result;
    }

    @Override
    final public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Category)) {
            return false;
        }

        Category category = (Category) o;

        if(id != null ? !id.equals(category.id) : category.id != null) {
            return false;
        }
        if(name != null ? !name.equals(category.name) : category.name != null) {
            return false;
        }
        return subcategories != null ? subcategories.equals(category.subcategories) : category.subcategories == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public List<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }
}
