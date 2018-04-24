package pl.shopgen.controllers.product;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

import java.util.ArrayList;
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
        List<String> id = new ArrayList<>();
        products.forEach(x -> id.add(x.getObjectId()));
        productRepository.deleteAllById(id);
        return products;
    }
}
