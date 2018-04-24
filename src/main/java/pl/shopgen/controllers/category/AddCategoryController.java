package pl.shopgen.controllers.category;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

@RestController
@RequestMapping("/categories")
public class AddCategoryController {
    private final CategoryRepository categoryRepository;

    public AddCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    Category addCategory(@RequestBody Category category) {
       return categoryRepository.insert(category);
    }
}
