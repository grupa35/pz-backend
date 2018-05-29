package pl.shopgen.controllers;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.builders.ApiErrorMessageBuilder;
import pl.shopgen.codes.ApiStatusCode;
import pl.shopgen.models.ErrorDTO;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleType;
import pl.shopgen.repositories.SaleRepository;

import java.time.LocalDate;

@RestController
@RequestMapping("/sales")
public class SaleController extends AbstractController {

    private final SaleRepository saleRepository;

    public SaleController(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }


    @RequestMapping(method = RequestMethod.POST)
    public String addSale(@RequestBody Sale sale) {
        if(sale.getCode() == null) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("code", "not exists");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getStartDate() == null || sale.getStartDate().isBefore(LocalDate.now())) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("startDate", "not exists or wrong date");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getEndDate() == null) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("endDate", "not exists or wrong date");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getSaleType() == null) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("saleType", "not exists");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getSaleType() == SaleType.PERCENT) {
            if(sale.getPercentValue() == 0) {
                ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
                errorMessageBuilder.badParameter("percentValue", "wrong value");
                return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
            }
        } else if(sale.getSaleType() == SaleType.NOMINAL) {
            if(sale.getNominalValue() == null) {
                ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
                errorMessageBuilder.badParameter("nominalValue", "not exist");
                return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
            }
        }
        return mapToJson(saleRepository.insert(sale));
    }

    @RequestMapping(value = "/{saleId}", method = RequestMethod.DELETE)
    public String deleteSale(@PathVariable("saleId") String saleId) {
        String jsonSale = mapToJson(saleRepository.findById(saleId).orElse(null));

        if(jsonSale == null) {
            return "";
        } else {
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
        String jsonSales = mapToJson(saleRepository.findById(saleId).orElse(null));

        if(jsonSales == null) {
            return "";
        }

        return jsonSales;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public String updateSale(@RequestBody Sale sale) {

        if(sale.getCode() == null || sale.getCode().equals("")) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("code", "Cannot create update sale by setting empty name");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getStartDate() == null || sale.getStartDate().isBefore(LocalDate.now())) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("startDate", "Cannot create update sale by setting wrong startDate");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getEndDate() == null) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("startDate", "Cannot create update sale by setting wrong endDate");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getSaleType() == null) {
            ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
            errorMessageBuilder.badParameter("startDate", "Cannot create update sale by setting empty saleType");
            return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
        } else if(sale.getSaleType() == SaleType.PERCENT) {
            if(sale.getPercentValue() == 0) {
                ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
                errorMessageBuilder.badParameter("startDate",
                        "Cannot create update sale by setting wrong percentValue when SaleType is set to PERCENT");
                return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
            }
        } else if(sale.getSaleType() == SaleType.NOMINAL) {
            if(sale.getNominalValue() == null) {
                ApiErrorMessageBuilder errorMessageBuilder = ApiErrorMessageBuilder.getInstance();
                errorMessageBuilder.badParameter("startDate",
                        "Cannot create update sale by setting empty nominalValue when SaleType is set to NOMINAL");
                return mapToJson(new ErrorDTO(ApiStatusCode.BAD_ARGUMENT, errorMessageBuilder.build()));
            }
        }
        return mapToJson(saleRepository.save(sale));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getSales() {
        return mapToJson(saleRepository.findAll());
    }
}
