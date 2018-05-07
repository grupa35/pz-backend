package pl.shopgen.controllers;

import javafx.util.converter.BigDecimalStringConverter;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.CategoryRepository;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class Search {
    private final ProductController productController;

    public Search(CategoryRepository categoryRepository, ProductRepository productRepository, ProductController productController) {
        this.productController = productController;
    }

    @RequestMapping(value = {"", " "}, method = RequestMethod.GET)
    @ResponseBody
    List<Product> findByName(@RequestParam Map<String,String> allRequestParams) {
        List<Product> productList = new ArrayList<Product>();
        String  productName = allRequestParams.getOrDefault("name", null);
        String  categoryName =allRequestParams.getOrDefault("category",null);
        String lowerPriceString = allRequestParams.getOrDefault("lowerPrice","0");
        String  higherPriceString =allRequestParams.getOrDefault("higherPrice","10000000");
        BigDecimal higherPrice=new BigDecimal(higherPriceString);
        BigDecimal lowerPrice=new BigDecimal(lowerPriceString);
        Predicate<Product> nameFilter = p ->   productName == null ? true :  p.getName().equalsIgnoreCase(productName);
        Predicate<Product> categoryFilter = p ->   categoryName == null ? true :  p.getCategory().getName().equalsIgnoreCase(categoryName);
        Predicate<Product> priceFilterLower = p ->   lowerPriceString == "0" ? true :  p.getPrice().compareTo(lowerPrice)>=1;
        Predicate<Product> priceFilterHigher = p -> higherPriceString == "10000000" ? true :  p.getPrice().compareTo(higherPrice)<=-1;
        productList = productController.getProducts()
                .stream()
                .filter(nameFilter)
                .filter(categoryFilter)
                .filter(priceFilterLower)
                .filter(priceFilterHigher)
                .collect(Collectors.toList());
        return productList;
    }

}
