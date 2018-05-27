package pl.shopgen.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import pl.shopgen.models.Category;
import pl.shopgen.repositories.CategoryRepository;

import java.util.List;
import java.util.Objects;

@Service
public class CategoryService implements ICategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(String categoryId) {
        if(categoryId == null) {
            return null;
        }

        List<Category> categories = categoryRepository.findAll();

        Category foundedCategory = categories.stream()
                .filter(category -> Objects.equals(category.getId(), categoryId))
                .findFirst()
                .orElse(null);

        if(foundedCategory == null) {
            foundedCategory = categories.stream()
                    .map(category -> category.getSubCategoryById(categoryId))
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);
        }

        return foundedCategory;
    }

    @Override
    public Category addNewCategory(Category category, String parentCategoryId) {
        if(parentCategoryId == null) {
            return addRootCategory(category);
        }

        return addChildCategory(category, parentCategoryId);
    }

    private Category addChildCategory(Category category, String parentCategoryId) {
        Category rootCategory = categoryRepository.findAll().stream()
                .filter(c -> c.getSubCategoryById(parentCategoryId) != null || c.getId().equals(parentCategoryId))
                .findFirst()
                .orElse(null);
        if(rootCategory == null) {
            return null;
        } else {
            category.setId(new ObjectId().toString());
            if(parentCategoryId.equals(rootCategory.getId())) {
                rootCategory.getSubcategories().add(category);
            } else {
                rootCategory.getSubCategoryById(parentCategoryId).getSubcategories().add(category);
            }
            categoryRepository.save(rootCategory);
            return category;
        }
    }

    private Category addRootCategory(Category category) {
        return categoryRepository.insert(category);
    }
}
