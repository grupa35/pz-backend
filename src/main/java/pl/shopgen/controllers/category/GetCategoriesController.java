package pl.shopgen.controllers.category;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class GetCategoriesController {

    private final CategoryRepository categoryRepository;


    public GetCategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Category> getCategories() {
        return categoryRepository.findAll();
    }


}
