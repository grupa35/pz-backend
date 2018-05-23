package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;
import pl.shopgen.models.SaleDTO;
import pl.shopgen.services.IProductSaleService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    private IProductSaleService productSaleService;

    public ProductController(ProductRepository productRepository, IProductSaleService productSaleService) {
        this.productRepository = productRepository;
        this.productSaleService = productSaleService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product product) {
        return productRepository.insert(product);
    }


    @RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
    public Product deleteProduct(@PathVariable("productId") String productId) {
        Product product = productRepository.findById(productId).orElse(null);

        if(product != null) {
            productRepository.deleteById(productId);
        }
        return product;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<Product> deleteProducts() {
        List<Product> products = productRepository.findAll();
        productRepository.deleteAll();
        return products;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Product updateProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @RequestMapping(value = "/{productId}/sale", method = RequestMethod.GET)
    @ResponseBody
    public SaleDTO getProductSale(@PathVariable("productId") String productId) {
        return productSaleService.getSaleDTO(getProduct(productId));
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Product getProduct(@PathVariable("productId") String productId) {
        return productRepository.findById(productId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
