package pl.shopgen.provider;

import java.math.BigDecimal;

import pl.shopgen.models.Product;

public interface IProductPriceProvider {

    BigDecimal getLowest(Product product);

    BigDecimal getSaleValue(Product product);
}
