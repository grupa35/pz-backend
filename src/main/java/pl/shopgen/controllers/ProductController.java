package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Product;
import pl.shopgen.repositories.ProductRepository;
import pl.shopgen.services.IProductSaleService;

@RestController
@RequestMapping("/products")
public class ProductController extends AbstractController {

    private final ProductRepository productRepository;

    private IProductSaleService productSaleService;

    public ProductController(ProductRepository productRepository, IProductSaleService productSaleService) {
        this.productRepository = productRepository;
        this.productSaleService = productSaleService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String addProduct(@RequestBody Product product) {
        return mapToJson(productRepository.insert(product));
    }


    @RequestMapping(value = "/{productId}", method = RequestMethod.DELETE)
    public String deleteProduct(@PathVariable("productId") String productId) {
        String jsonProduct = mapToJson(productRepository.findById(productId).orElse(null));

        if(jsonProduct.isEmpty()) {
            productRepository.deleteById(productId);
        }
        return jsonProduct;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteProducts() {
        String jsonProducts = mapToJson(productRepository.findAll());
        productRepository.deleteAll();
        return jsonProducts;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateProduct(@RequestBody Product product) {
        return mapToJson(productRepository.save(product));
    }

    @RequestMapping(value = "/{productId}/sale", method = RequestMethod.GET)
    @ResponseBody
    public String getProductSale(@PathVariable("productId") String productId) {
        return mapToJson(productSaleService.getSaleDTO(productRepository.findById(productId).orElse(null)));
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public String getProduct(@PathVariable("productId") String productId) {
        return mapToJson(productRepository.findById(productId).orElse(null));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getProducts() {
        return mapToJson(productRepository.findAll());
    }
}
