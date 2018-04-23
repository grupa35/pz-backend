package pl.shopgen.controllers.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class DeleteCategories {

    private final CategoryRepository categoryRepository;

    public DeleteCategories(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    List<Category> deleteCategories()
    {
        List<Category> categories = categoryRepository.findAll();
        List<String> id = new ArrayList<>();
        categories.forEach(x -> id.add(x.getId()));
        categoryRepository.deleteAllById(id);
        return categories;
    }
}
