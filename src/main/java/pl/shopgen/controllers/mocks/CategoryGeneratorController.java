package pl.shopgen.controllers.mocks;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;
import pl.shopgen.models.mocks.CategoryGenerator;

import java.util.List;

@RestController
@RequestMapping("/mocks/categoryGenerator")
public class CategoryGeneratorController {

    private final CategoryRepository categoryRepository;

    private final CategoryGenerator categoryGenerator;

    public CategoryGeneratorController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        categoryGenerator = new CategoryGenerator();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Category> categoryGenerator() {
        return categoryRepository.insert(categoryGenerator.generateCategories());
    }
}
