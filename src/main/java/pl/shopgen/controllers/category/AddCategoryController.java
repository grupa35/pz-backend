package pl.shopgen.controllers.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import java.net.URI;

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
