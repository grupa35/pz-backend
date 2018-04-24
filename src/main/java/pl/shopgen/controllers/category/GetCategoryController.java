package pl.shopgen.controllers.category;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

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
