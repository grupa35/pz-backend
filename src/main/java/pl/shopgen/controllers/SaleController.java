package pl.shopgen.controllers;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Sale;
import pl.shopgen.repositories.SaleRepository;

@RestController
@RequestMapping("/sales")
public class SaleController extends AbstractController {

    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addSale(@RequestBody Sale sale) {
        return mapToJson(saleRepository.insert(sale));
    }

    @RequestMapping(value = "/{saleId}", method = RequestMethod.DELETE)
    public String deleteSale(@PathVariable("saleId") String saleId) {
        String jsonSale = mapToJson(saleRepository.findById(saleId).orElse(null));

        if(jsonSale.isEmpty()) {
            saleRepository.deleteById(saleId);
        }

        return jsonSale;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteSales() {
        String jsonSales = mapToJson(saleRepository.findAll());
        saleRepository.deleteAll();
        return jsonSales;
    }

    @RequestMapping(value = "/{saleId}", method = RequestMethod.GET)
    @ResponseBody
    public String getSale(@PathVariable("saleId") String saleId) {
        return mapToJson(saleRepository.findById(saleId).orElse(null));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateSale(@RequestBody Sale sale) {
        return mapToJson(saleRepository.save(sale));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSales() {
        return mapToJson(saleRepository.findAll());
    }
}
