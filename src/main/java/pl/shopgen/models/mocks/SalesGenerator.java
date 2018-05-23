package pl.shopgen.models.mocks;

import pl.shopgen.models.ProductRepository;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SalesGenerator {

    private final ProductRepository productRepository;

    private final Random random;

    public SalesGenerator(ProductRepository productRepository) {
        this.productRepository = productRepository;
        random = new Random();
    }


    public List<Sale> generateSales() {
        List<Sale> sales = new ArrayList<>();

        productRepository.findAll().stream()
                         .limit(10)
                         .collect(Collectors.toList())
                         .forEach(product -> sales.add(generateSale(product.getId())));


        return sales;
    }

    public Sale generateSale(String productId) {
        Sale sale = new Sale();

        sale.setActive(true);
        sale.setStartDate(LocalDate.now());
        sale.setEndDate(LocalDate.now().plusDays(random.nextInt(50)));
        sale.setCode(String.valueOf(random.nextInt()));
        sale.setNominalValue(new BigDecimal(random.nextDouble() * 100));
        sale.setPercentValue(random.nextDouble() * 100);
        sale.setProductId(productId);
        sale.setSaleType(SaleType.values()[random.nextInt(SaleType.values().length)]);

        return sale;
    }
}

