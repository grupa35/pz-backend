package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    Category addCategory(@RequestBody Category category) {
        return categoryRepository.insert(category);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    List<Category> deleteCategories() {
        List<Category> categories = categoryRepository.findAll();
        categoryRepository.deleteAll();
        return categories;
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    Category deleteCategory(@PathVariable("categoryId") String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElse(null);

        if(category != null) {
            categoryRepository.deleteById(categoryId);
        }

        return category;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    Category getCategory(@PathVariable("categoryId") String categoryId) {
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.PUT)
    Category updateCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
}
