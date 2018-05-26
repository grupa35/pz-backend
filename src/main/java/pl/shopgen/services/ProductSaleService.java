package pl.shopgen.services;

import org.springframework.stereotype.Service;
import pl.shopgen.models.Product;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleDTO;
import pl.shopgen.repositories.SaleRepository;
import pl.shopgen.models.SaleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ProductSaleService implements IProductSaleService {

    private SaleRepository saleRepository;

    private LocalDate now;

    private List<Sale> currentSalesForProduct = new ArrayList<>();

    public ProductSaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public BigDecimal getLowestProductPrice(Product product) {
        return createSaleContext(product).getLowestPrice();
    }

    @Override
    public BigDecimal getSaleValue(Product product) {
        return createSaleContext(product).getSaleValue();
    }

    @Override
    public SaleDTO getSaleDTO(Product product) {
        if(product == null) {
            return SaleDTO.builder()
                    .isSale(false)
                    .build();
        }

        SaleContext saleContext = createSaleContext(product);
        if(saleContext == null || saleContext.getSale() == null) {
            return SaleDTO.builder()
                    .isSale(false)
                    .build();
        }

        return SaleDTO.builder()
                .isSale(true)
                .code(saleContext.getSale().getCode())
                .saleType(saleContext.getSale().getSaleType())
                .saleNominalValue(saleContext.getSale().getNominalValue())
                .salePercentValue(saleContext.getSale().getPercentValue())
                .priceAfterSale(saleContext.getLowestPrice())
                .saleValue(saleContext.getSaleValue())
                .build();
    }

    @Override
    public Sale getBiggestSale(Product product) {
        return createSaleContext(product).getSale();
    }

    private SaleContext createSaleContext(Product product) {
        now = LocalDate.now();
        initCurrentSalesForProduct(product);

        Sale biggestSale = findBiggestSale(product);

        SaleContext saleContext = new SaleContext();
        saleContext.setSale(biggestSale);
        saleContext.setLowestPrice(getProductPriceAfterSale(product, biggestSale));
        saleContext.setSaleValue(getSaleValueFromProduct(product, biggestSale));

        return saleContext;
    }

    private void initCurrentSalesForProduct(Product product) {
        currentSalesForProduct.clear();
        if(product != null) {
            currentSalesForProduct.addAll(saleRepository.findAllByProductId(product.getId()));
        }
    }

    private BigDecimal getProductPriceAfterSale(Product product, Sale sale) {
        if(product == null) {
            return BigDecimal.ZERO;
        }

        if(sale == null) {
            return product.getPrice();
        }

        return product.getPrice().subtract(getSaleValueFromProduct(product, sale));
    }

    private BigDecimal getSaleValueFromProduct(Product product, Sale sale) {
        if(product == null || sale == null) {
            return BigDecimal.ZERO;
        }

        if(sale.getSaleType() == SaleType.PERCENT) {
            return getPercentPriceCut(product, sale);
        } else {
            return getNominalPercentCut(sale);
        }
    }

    private Sale findBiggestSale(Product product) {
        Sale percentSale = getPercentSale(product);
        Sale nominalSale = getNominalSale(product);

        BigDecimal percentPriceCut = getPercentPriceCut(product, percentSale);
        BigDecimal nominalPercentCut = getNominalPercentCut(nominalSale);

        if(percentPriceCut.compareTo(nominalPercentCut) >= 0) {
            return percentSale;
        } else {
            return nominalSale;
        }
    }

    private Sale getNominalSale(Product product) {
        return currentSalesForProduct.stream()
                .filter(sale -> sale.getSaleType() == SaleType.NOMINAL)
                .filter(sale -> sale.getStartDate().compareTo(now) <= 0)
                .filter(sale -> sale.getEndDate().compareTo(now) >= 0)
                .filter(Sale::isActive)
                .filter(sale -> product.getPrice().subtract(sale.getNominalValue()).compareTo(BigDecimal.ZERO) > 0)
                .min(Comparator.comparing(Sale::getNominalValue))
                .orElse(null);
    }

    private Sale getPercentSale(Product product) {
        return currentSalesForProduct.stream()
                .filter(sale -> sale.getSaleType() == SaleType.PERCENT)
                .filter(sale -> sale.getStartDate().compareTo(now) <= 0)
                .filter(sale -> sale.getEndDate().compareTo(now) >= 0)
                .filter(Sale::isActive)
                .filter(sale -> product.getPrice()
                        .multiply(BigDecimal.valueOf(1.0 - sale.getPercentValue() / 100.0))
                        .compareTo(BigDecimal.ZERO) > 0)
                .max(Comparator.comparingDouble(Sale::getPercentValue))
                .orElse(null);
    }

    private BigDecimal getNominalPercentCut(Sale nominalSale) {
        return Optional.ofNullable(nominalSale)
                .map(Sale::getNominalValue)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getPercentPriceCut(Product product, Sale sale) {
        if(product == null || sale == null) {
            return BigDecimal.ZERO;
        }

        return product.getPrice().multiply(BigDecimal.valueOf(Optional.of(sale)
                .map(Sale::getPercentValue)
                .orElse(0.0) / 100.0));
    }

    private class SaleContext {

        private Sale sale;

        private BigDecimal lowestPrice;

        private BigDecimal saleValue;

        public Sale getSale() {
            return sale;
        }

        public void setSale(Sale sale) {
            this.sale = sale;
        }

        public BigDecimal getSaleValue() {
            return saleValue;
        }

        public void setSaleValue(BigDecimal saleValue) {
            this.saleValue = saleValue;
        }

        public BigDecimal getLowestPrice() {
            return lowestPrice;
        }

        public void setLowestPrice(BigDecimal lowestPrice) {
            this.lowestPrice = lowestPrice;
        }
    }
}
