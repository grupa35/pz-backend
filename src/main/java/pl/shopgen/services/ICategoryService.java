package pl.shopgen.services;

import pl.shopgen.models.Category;

public interface ICategoryService {

    Category getCategoryById(String categoryId);

    Category addNewCategory(Category category, String parentCategoryId);
}
