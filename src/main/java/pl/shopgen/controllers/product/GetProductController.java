package pl.shopgen.controllers.product;


import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;


@RestController
@RequestMapping("/products")
public class GetProductController {
    private final ProductRepository productRepository;

    public GetProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    Product getProduct(@PathVariable("productId") String productId) {
        return productRepository.findById(productId).get();
    }
}
