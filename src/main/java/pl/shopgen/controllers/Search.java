package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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
    private final ProductRepository productRepository;

    public Search(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(value = {"", " "}, method = RequestMethod.GET)
    @ResponseBody
    List<Product> search(@RequestParam Map<String, String> allRequestParams) {
        List<Product> productList = new ArrayList<Product>();
        BigDecimal higherPrice;
        BigDecimal lowerPrice;
        String productName = allRequestParams.getOrDefault("name", null);
        String idCategory = allRequestParams.getOrDefault("category", null);
        String lowerPriceString = allRequestParams.getOrDefault("lowerPrice", null);
        String higherPriceString = allRequestParams.getOrDefault("higherPrice", null);
        lowerPrice = createBigDecimal(lowerPriceString);
        higherPrice = createBigDecimal(higherPriceString);
        Predicate<Product> nameFilter = p -> productName == null ? true : p.getName().equalsIgnoreCase(productName);
        Predicate<Product> categoryFilter = p -> idCategory == null ? true : p.getCategory().getId().equals(idCategory);
        Predicate<Product> priceFilterLower = p -> lowerPrice == null ? true : p.getPrice().compareTo(lowerPrice)>= 0;
        Predicate<Product> priceFilterHigher = p -> higherPrice == null ? true : p.getPrice().compareTo(higherPrice)<= 0;
        productList = productRepository.findAll()
                .stream()
                .filter(nameFilter)
                .filter(categoryFilter)
                .filter(priceFilterLower)
                .filter(priceFilterHigher)
                .collect(Collectors.toList());


        return productList;
    }

    public static BigDecimal createBigDecimal(String str) {
        if(str == null) {
            return null;
        }
        return new BigDecimal(str);
    }

}
