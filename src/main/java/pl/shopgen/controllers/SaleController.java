package pl.shopgen.controllers;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleRepository;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    @RequestMapping(method = RequestMethod.POST)
    public Sale addSale(@RequestBody Sale sale) {
        return saleRepository.insert(sale);
    }

    @RequestMapping(value = "/{saleId}", method = RequestMethod.DELETE)
    public Sale deleteSale(@PathVariable("saleId") String saleId) {
        Sale sale = saleRepository.findById(saleId).orElse(null);

        if(sale != null) {
            saleRepository.deleteById(saleId);
        }

        return sale;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public List<Sale> deleteSales() {
        List<Sale> sales = saleRepository.findAll();
        saleRepository.deleteAll();
        return sales;
    }

    @RequestMapping(value = "/{saleId}", method = RequestMethod.GET)
    @ResponseBody
    public Sale getSale(@PathVariable("saleId") String saleId) {
        return saleRepository.findById(saleId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Sale updateSale(@RequestBody Sale sale) {
        return saleRepository.save(sale);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Sale> getSales() {
        return saleRepository.findAll();
    }
}
