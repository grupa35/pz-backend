package pl.shopgen.services;

import pl.shopgen.models.Category;

public interface ICategoryService {

    Category getById(String categoryId);

    Category addNew(Category category, String parentCategoryId);

    Category update(Category category);

    Category deleteById(String categoryId);
}
