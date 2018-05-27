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
import pl.shopgen.models.CategoryDTO;
import pl.shopgen.repositories.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CategoryDTO addCategory(@RequestBody Map<String, String> categoryNameMap, List<Category> categorySubcategory) {
        CategoryDTO categoryDTO;
        String categoryName = categoryNameMap.getOrDefault("name", null);
        if(categoryName == null) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setStatus(ApiStatusCode.BAD_ARGUMENT);
            categoryDTO
                    .setErrorMessage(ApiErrorMessageBuilder.getInstance().badParameter("name", "not exists").build());
            return categoryDTO;
        } else if(categoryRepository.findByName(categoryName).isPresent()) {
            categoryDTO = new CategoryDTO(categoryRepository.findByName(categoryName).orElse(null));
            categoryDTO.setStatus(ApiStatusCode.OBJECT_EXISTS);
            categoryDTO.setErrorMessage(ApiErrorMessageBuilder.getInstance().badParameter("name", "exists").build());
        } else {
            categoryDTO = new CategoryDTO(categoryRepository.insert(new Category(categoryName, categorySubcategory)));
        }
        return categoryDTO;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<CategoryDTO> deleteCategories() {
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        List<Category> categories = categoryRepository.findAll();

        if(categories.isEmpty()) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setStatus(ApiStatusCode.NOT_FOUND);
        } else {
            categoryRepository.deleteAll();
            categories.forEach(category -> {
                CategoryDTO categoryDTO = new CategoryDTO(category);
                categoryDTO.setStatus(ApiStatusCode.SUCCESS);
                categoryDTOs.add(categoryDTO);
            });
        }
        return categoryDTOs;
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    public CategoryDTO deleteCategory(@PathVariable("categoryId") String categoryId) {
        CategoryDTO categoryDTO;

        Category category = categoryRepository.findById(categoryId).orElse(null);
        if(category != null) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setStatus(ApiStatusCode.NOT_FOUND);
        } else {
            categoryRepository.deleteById(categoryId);
            categoryDTO = new CategoryDTO(category);
            categoryDTO.setStatus(
                    ApiStatusCode.SUCCESS
            );
        }

        return categoryDTO;
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET)
    @ResponseBody
    public CategoryDTO getCategory(@PathVariable("categoryId") String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        CategoryDTO categoryDTO = new CategoryDTO();

        if(category == null) {
            categoryDTO.setStatus(ApiStatusCode.NOT_FOUND);
            categoryDTO.setErrorMessage(ApiErrorMessageBuilder.getInstance()
                    .notFound("Not found category with id: " + categoryId).build());
        } else {
            categoryDTO.setStatus(ApiStatusCode.SUCCESS);
        }
        return categoryDTO;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public CategoryDTO updateCategory(@RequestBody Category category) {
        CategoryDTO categoryDTO;

        if(category == null) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setStatus(ApiStatusCode.BAD_ARGUMENT);
        } else if(category.getName() == null || category.getName().equals("")) {
            categoryDTO = new CategoryDTO();
            categoryDTO.setStatus(ApiStatusCode.BAD_ARGUMENT);
            categoryDTO.setErrorMessage(ApiErrorMessageBuilder.getInstance()
                    .badParameter("name", "Cannot create update category by setting empty name").build());
        } else {
            categoryDTO = new CategoryDTO(categoryRepository.save(category));
            categoryDTO.setStatus(ApiStatusCode.SUCCESS);
        }
        return categoryDTO;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<CategoryDTO> getCategories() {
        List<CategoryDTO> categoryDTOs = new ArrayList<>();

        categoryRepository.findAll().forEach(category -> {
            CategoryDTO categoryDTO = new CategoryDTO(category);
            categoryDTO.setStatus(ApiStatusCode.SUCCESS);
            categoryDTOs.add(categoryDTO);
        });
        return categoryDTOs;
    }
}
