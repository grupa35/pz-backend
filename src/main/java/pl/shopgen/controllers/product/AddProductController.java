package pl.shopgen.controllers.product;


import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

@RestController
@RequestMapping("/products")
public class AddProductController {
    private final ProductRepository productRepository;

    public AddProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    Product addProduct(@RequestBody Product product){
        return productRepository.insert(product);
    }
}
