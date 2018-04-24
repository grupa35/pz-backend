package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    Product addProduct(@RequestBody Product product) {
        return productRepository.insert(product);
    }


    @RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
    Product deleteProduct(@PathVariable("productId") String productId) {
        Product product = productRepository.findById(productId).orElse(null);

        if(product != null) {
            productRepository.deleteById(productId);
        }
        return product;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    List<Product> deleteProducts() {
        List<Product> products = productRepository.findAll();
        productRepository.deleteAll();
        return products;
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    Product getProduct(@PathVariable("productId") String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    List<Product> getProducts() {
        return productRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.PUT)
    Product updateProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }
}
