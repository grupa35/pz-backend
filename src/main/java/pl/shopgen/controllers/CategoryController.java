package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.builders.ApiErrorMessageBuilder;
import pl.shopgen.codes.ApiStatusCode;
import pl.shopgen.models.Category;
import pl.shopgen.models.ErrorDTO;
import pl.shopgen.repositories.CategoryRepository;
import pl.shopgen.services.ICategoryService;

import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController extends AbstractController {

    private final CategoryRepository categoryRepository;

    private final ICategoryService categoryService;

    public CategoryController(CategoryRepository categoryRepository, ICategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addCategory(@RequestBody Map<String, String> requestBody) {
        String categoryName = requestBody.getOrDefault("name", null);
        String parentCategoryId = requestBody.getOrDefault("parentCategoryId", null);
        if(categoryName == null) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("name", "not exists");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(parentCategoryId != null && categoryService.getById(parentCategoryId) == null) {
            return mapToJson(new ErrorDTO(ApiStatusCode.OBJECT_EXISTS,
                    ApiErrorMessageBuilder.getInstance().notFound("Not found parent category by requested id")
                            .build()));
        } else {
            Category category = new Category();
            category.setName(categoryName);
            return mapToJson(categoryService.addNew(category, parentCategoryId));
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteCategories() {
        String jsonCategories = mapToJson(categoryRepository.findAll());
        categoryRepository.deleteAll();
        return jsonCategories;
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    public String deleteCategory(@PathVariable("categoryId") String categoryId) {
        return mapToJson(categoryService.deleteById(categoryId));
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public String getCategory(@PathVariable("categoryId") String categoryId) {
        return mapToJson(categoryService.getById(categoryId));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateCategory(@RequestBody Category category) {
        if(category.getName() == null || category.getName().equals("")) {
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "Cannot create update category by setting empty name").build()));
        } else {
            return mapToJson(categoryService.update(category));
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getCategories() {
        return mapToJson(categoryRepository.findAll());
    }
}
