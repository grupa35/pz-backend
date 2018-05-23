package pl.shopgen.services;

import pl.shopgen.models.Product;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleDTO;

import java.math.BigDecimal;

public interface IProductSaleService {

    BigDecimal getLowestProductPrice(Product product);

    BigDecimal getSaleValue(Product product);

    SaleDTO getSaleDTO(Product product);

    Sale getBiggestSale(Product product);
}
