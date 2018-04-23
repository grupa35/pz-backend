package pl.shopgen.controllers.category;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class GetCategoryController {

    private final CategoryRepository categoryRepository;


    public GetCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    Category getCategory(@PathVariable("categoryId") String categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

//    ResponseEntity<?> getCategory(@PathVariable("categoryId") String categoryId) {
//        Category category =categoryRepository.findById(categoryId).get();
//        return new ResponseEntity<Category>(category, HttpStatus.OK);
//    }

}
