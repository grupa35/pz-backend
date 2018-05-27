package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Category;
import pl.shopgen.models.Product;
import pl.shopgen.repositories.ProductRepository;
import pl.shopgen.services.ICategoryService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class SearchController extends AbstractController {

    private final ProductRepository productRepository;

    private final ICategoryService categoryService;

    public SearchController(ProductRepository productRepository, ICategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }


    @RequestMapping(value = {"", " "}, method = RequestMethod.GET)
    @ResponseBody
    public String search(@RequestParam Map<String, String> allRequestParams) {
        ProductFiltersBuilder builder = new ProductFiltersBuilder(allRequestParams, categoryService);
        List<Predicate<Product>> productFilters = builder
                .name()
                .category()
                .lowerPrice()
                .higherPrice()
                .build();

        return mapToJson(productRepository.findAll().stream()
                .filter(product -> productFilters.stream()
                        .allMatch(productPredicate -> productPredicate.test(product)))
                .collect(Collectors.toList()));
    }


    private static class ProductFiltersBuilder {

        private List<Predicate<Product>> productFilters;

        private Map<String, String> allRequestParams;

        private ICategoryService categoryService;

        ProductFiltersBuilder(Map<String, String> allRequestParams, ICategoryService categoryService) {
            this.allRequestParams = allRequestParams;
            this.categoryService = categoryService;
            productFilters = new ArrayList<>();
        }

        public ProductFiltersBuilder name() {
            String productName = allRequestParams.getOrDefault("name", null);
            productFilters.add(product -> productName == null || product.getName().toLowerCase().contains(productName.toLowerCase()));
            return this;
        }

        public ProductFiltersBuilder category() {
            Category requestCategory = categoryService
                    .getCategoryById(allRequestParams.getOrDefault("categoryId", null));

            productFilters.add(product -> {
                if(allRequestParams.getOrDefault("categoryId", null) == null) {
                    return true;
                }

                if(requestCategory == null) {
                    return false;
                }

                if(product.getCategory() == null) {
                    return false;
                }

                if(Objects.equals(requestCategory.getId(), product.getCategory().getId())) {
                    return true;
                }

                return requestCategory.getSubCategoryById(product.getCategory().getId()) != null;
            });

            return this;
        }

        public ProductFiltersBuilder lowerPrice() {
            BigDecimal lowerPrice = parseToBigDecimal(allRequestParams.getOrDefault("lowerPrice", null));
            productFilters.add(product -> lowerPrice == null || product.getPrice().compareTo(lowerPrice) >= 0);
            return this;
        }

        public static BigDecimal parseToBigDecimal(String str) {
            if(str == null) {
                return null;
            }
            return new BigDecimal(str);
        }

        public ProductFiltersBuilder higherPrice() {
            BigDecimal higherPrice = parseToBigDecimal(allRequestParams.getOrDefault("higherPrice", null));
            productFilters.add(product -> higherPrice == null || product.getPrice().compareTo(higherPrice) <= 0);
            return this;
        }

        public List<Predicate<Product>> build() {
            return productFilters;
        }
    }
}
