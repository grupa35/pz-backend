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
    public Category getById(String categoryId) {
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
    public Category addNew(Category category, String parentCategoryId) {
        if(parentCategoryId == null) {
            return addRootCategory(category);
        }

        return addChildCategory(category, parentCategoryId);
    }

    @Override
    public Category update(Category category) {
        if(category == null) {
            return null;
        }

        Category root = getRoot(category.getId());
        if(root != null) {
            if(root.getId().equals(category.getId())) {
                return categoryRepository.save(category);
            } else {
                root.getSubCategoryById(category.getId()).update(category);
                return categoryRepository.save(root);
            }
        } else {
            return null;
        }
    }

    @Override
    public Category deleteById(String categoryId) {
        Category root = getRoot(categoryId);


        if(root != null) {
            if(root.getId().equals(categoryId)) {
                categoryRepository.delete(root);
                return root;
            }
            Category parent = root.getParentOfById(categoryId);

            if(parent != null) {
                Category categoryToDelete = parent.getSubCategoryById(categoryId);
                parent.getSubcategories().remove(categoryToDelete);
                categoryRepository.save(root);
                return categoryToDelete;
            }
        }
        return null;
    }

    private Category addChildCategory(Category category, String parentCategoryId) {
        Category rootCategory = getRoot(parentCategoryId);
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

    private Category getRoot(String categoryId) {
        return categoryRepository.findAll().stream()
                .filter(c -> c.getSubCategoryById(categoryId) != null || c.getId().equals(categoryId))
                .findFirst()
                .orElse(null);
    }

    private Category addRootCategory(Category category) {
        return categoryRepository.insert(category);
    }
}
