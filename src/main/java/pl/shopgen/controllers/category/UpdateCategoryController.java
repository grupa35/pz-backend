package pl.shopgen.controllers.category;

import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class UpdateCategoryController {
    private final CategoryRepository categoryRepository;

    public UpdateCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.PUT)
    Category updateCategory(@RequestBody Category category)
    {
        Category cat = categoryRepository.findById(category.getId()).orElseThrow(NullPointerException::new);
        cat.setName(category.getName());
        cat.setSubcategories(category.getSubcategories());

        return categoryRepository.save(cat);

    }
}
