package pl.shopgen.controllers.product;

import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

@RestController
@RequestMapping("/products/{product_id}")
public class DeleteProduct {
    private final ProductRepository productRepository;

    public DeleteProduct(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @RequestMapping(method = RequestMethod.DELETE)
    Product deleteProduct(@PathVariable("product_id")String productId){
        Product product = productRepository.findById(productId).get();
        productRepository.deleteById(product.getObjectId());
        return product;
    }

}
