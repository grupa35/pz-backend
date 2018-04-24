package pl.shopgen.controllers.product;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/products")
public class DeleteProducts {
    private final ProductRepository productRepository;

    public DeleteProducts(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @RequestMapping(method = RequestMethod.DELETE)
    List<Product> deleteProducts()
    {
        List<Product> products = productRepository.findAll();
        productRepository.deleteAll(products);
        return products;
    }
}
