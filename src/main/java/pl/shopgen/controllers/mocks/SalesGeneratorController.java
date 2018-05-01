package pl.shopgen.controllers.mocks;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.ProductRepository;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleRepository;
import pl.shopgen.models.mocks.SalesGenerator;

import java.util.List;

@RestController
@RequestMapping("/mocks/salesGenerator")
public class SalesGeneratorController {

    private final ProductRepository productRepository;

    private final SalesGenerator salesGenerator;

    private final SaleRepository saleRepository;

    public SalesGeneratorController(ProductRepository productRepository, SaleRepository saleRepository) {
        this.productRepository = productRepository;
        this.saleRepository = saleRepository;
        salesGenerator = new SalesGenerator(productRepository);
    }

    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public Sale generateSale(@PathVariable("productId") String productId) {
        return saleRepository.insert(salesGenerator.generateSale(productId));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Sale> generateSales() {
        return saleRepository.insert(salesGenerator.generateSales());
    }
}
