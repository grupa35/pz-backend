package pl.shopgen.controllers.product;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

import java.util.List;


@RestController
@RequestMapping("/products")
public class GetProductsController {

    private final ProductRepository productRepository;


    public GetProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Product> getProducts() {
        return productRepository.findAll();
    }
}