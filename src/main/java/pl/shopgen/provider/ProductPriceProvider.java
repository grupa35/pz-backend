package pl.shopgen.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import pl.shopgen.models.Product;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleRepository;

public class ProductPriceProvider implements IProductPriceProvider {

    private SaleRepository saleRepository;

    private LocalDate now;

    public ProductPriceProvider(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public BigDecimal getLowest(Product product) {
        now = LocalDate.now();
        return product.getPrice().subtract(getSaleValue(product)).setScale(2, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal getSaleValue(Product product) {
        BigDecimal productPrice = product.getPrice().setScale(2, RoundingMode.HALF_DOWN);

        BigDecimal percentSalePriceCut = productPrice.multiply(BigDecimal.valueOf(getSalePercentValue(product) / 100.0));
        BigDecimal nominalSalePriceCut = getSaleNominalValue(product);

        if(nominalSalePriceCut.compareTo(percentSalePriceCut) < 0) {
            return percentSalePriceCut.setScale(2, RoundingMode.HALF_DOWN);
        } else {
            return nominalSalePriceCut.setScale(2, RoundingMode.HALF_DOWN);
        }
    }

    private BigDecimal getSaleNominalValue(Product product) {
        return saleRepository.findAllByProductId(product.getId())
                .stream()
                .filter(sale -> sale.getStartDate().compareTo(now) <= 0)
                .filter(sale -> sale.getEndDate().compareTo(now) >= 0)
                .filter(Sale::isActive)
                .map(Sale::getNominalValue)
                .filter(nominalValue -> product.getPrice().subtract(nominalValue).compareTo(BigDecimal.ZERO) > 0)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
    }

    private double getSalePercentValue(Product product) {
        return saleRepository.findAllByProductId(product.getId())
                .stream()
                .filter(sale -> sale.getStartDate().compareTo(now) <= 0)
                .filter(sale -> sale.getEndDate().compareTo(now) >= 0)
                .filter(Sale::isActive)
                .map(Sale::getPercentValue)
                .filter(percentValue -> product.getPrice()
                        .multiply(BigDecimal.valueOf(1.0 - percentValue / 100.0))
                        .compareTo(BigDecimal.ZERO) > 0)
                .max(Double::compareTo)
                .orElse(0.0);
    }
}
