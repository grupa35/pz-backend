package pl.shopgen.controllers.product;

import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;


@RestController
@RequestMapping("/products")
public class UpdateProductController {
    private final ProductRepository productRepository;

    public UpdateProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.PUT)
    Product updateProduct(@RequestBody Product product) {
        Product product1 = productRepository.findById(product.getObjectId()).orElseThrow(NullPointerException::new);
        product1.setName(product.getName());
        product1.setPrice(product.getPrice());
        product1.setImgUrl(product.getImgUrl());
        product1.setCategory(product.getCategory());
        product1.setSizeToAmountMap(product.getSizeToAmountMap());
        product1.setDescription(product.getDescription());
        return productRepository.save(product1);
    }
}