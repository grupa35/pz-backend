package pl.shopgen.controllers.category;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

@RestController
@RequestMapping("/categories/{category_id}")
public class DeleteCategory {
    private final CategoryRepository categoryRepository;

    public DeleteCategory(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    Category deleteCategory(@PathVariable("category_id") String categoryId) {
       Category  category =  categoryRepository.findById(categoryId).get();

           categoryRepository.deleteById(category.getId());
       return category;
    }
}
