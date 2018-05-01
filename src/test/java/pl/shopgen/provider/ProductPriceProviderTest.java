package pl.shopgen.provider;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleRepository;
import pl.shopgen.models.mocks.ProductListGenerator;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductPriceProviderTest {

    private IProductPriceProvider productPriceProvider;

    private Product insertedProduct;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Before
    public void init() {
        productPriceProvider = new ProductPriceProvider(saleRepository);
        initProduct();
    }

    private void initProduct() {
        ProductListGenerator productListGenerator = new ProductListGenerator();
        insertedProduct = productRepository.insert(productListGenerator.generateProducts().get(0));
    }


    @Test
    public void lowestPriceWhenNominalSaleHigherThanPercentSaleTest() {
        BigDecimal nominalSaleCutPrice = insertedProduct.getPrice()
                .divide(BigDecimal.valueOf(18), RoundingMode.HALF_DOWN)
                .setScale(2, RoundingMode.HALF_DOWN);

        Sale sale = getDefaultSaleMock();
        sale.setPercentValue(5);
        sale.setNominalValue(nominalSaleCutPrice);

        saleRepository.insert(sale);
        Assert.assertEquals(insertedProduct.getPrice().subtract(nominalSaleCutPrice),
                productPriceProvider.getLowest(insertedProduct));
    }

    private Sale getDefaultSaleMock() {
        Sale sale = new Sale();
        sale.setPercentValue(25);
        sale.setNominalValue(insertedProduct.getPrice()
                .divide(BigDecimal.valueOf(3), RoundingMode.HALF_DOWN)
                .setScale(2, RoundingMode.HALF_DOWN));
        sale.setStartDate(LocalDate.now());
        sale.setEndDate(LocalDate.now().plusDays(7));
        sale.setActive(true);
        sale.setCode("xyz");
        sale.setProductId(insertedProduct.getId());

        return sale;
    }

    @Test
    public void lowestPriceWhenPercentSaleHigherThanNominalSaleTest() {
        BigDecimal percentSaleCutPrice = insertedProduct.getPrice()
                .divide(BigDecimal.valueOf(2), RoundingMode.HALF_DOWN);
        BigDecimal nominalSaleCutPrice = insertedProduct.getPrice().divide(BigDecimal.TEN, RoundingMode.HALF_DOWN);

        Sale sale = getDefaultSaleMock();
        sale.setPercentValue(50);
        sale.setNominalValue(nominalSaleCutPrice);

        saleRepository.insert(sale);
        Assert.assertEquals(insertedProduct.getPrice().subtract(percentSaleCutPrice),
                productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenNoSaleTest() {
        Assert.assertEquals(insertedProduct.getPrice(), productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenManySalesTest() {
        BigDecimal higherNominalSaleValue = insertedProduct.getPrice()
                .divide(BigDecimal.valueOf(20), RoundingMode.HALF_DOWN)
                .setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal lowerNominalSaleValue = insertedProduct.getPrice()
                .divide(BigDecimal.valueOf(10), RoundingMode.HALF_DOWN)
                .setScale(2, RoundingMode.HALF_DOWN);

        Sale higher = getDefaultSaleMock();
        Sale lower = getDefaultSaleMock();

        higher.setNominalValue(higherNominalSaleValue);
        higher.setPercentValue(0);

        lower.setNominalValue(lowerNominalSaleValue);
        lower.setPercentValue(0);

        saleRepository.insert(Arrays.asList(higher, lower));

        Assert.assertEquals(insertedProduct.getPrice().subtract(higherNominalSaleValue),
                productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenPercentSaleIsNotSetTest() {
        Sale sale = getDefaultSaleMock();
        sale.setPercentValue(0.0);
        BigDecimal saleNominalValue = insertedProduct.getPrice()
                .divide(BigDecimal.TEN, RoundingMode.HALF_DOWN)
                .setScale(2, RoundingMode.HALF_DOWN);

        sale.setNominalValue(saleNominalValue);
        saleRepository.insert(sale);

        Assert.assertEquals(insertedProduct.getPrice().subtract(saleNominalValue),
                productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenNominalSaleIsNotSetTest() {
        double percentSaleValue = 25;
        BigDecimal percentSaleCutPrice = insertedProduct.getPrice()
                .multiply(BigDecimal.valueOf(percentSaleValue / 100.0))
                .setScale(2, RoundingMode.HALF_DOWN);

        Sale sale = getDefaultSaleMock();
        sale.setNominalValue(BigDecimal.ZERO);
        sale.setPercentValue(percentSaleValue);

        saleRepository.insert(sale);

        Assert.assertEquals(insertedProduct.getPrice().subtract(percentSaleCutPrice),
                productPriceProvider.getLowest(insertedProduct));

    }

    @Test
    public void lowestPriceWhenAllSalesAreNotActiveTest() {
        Sale sale1 = getDefaultSaleMock();
        Sale sale2 = getDefaultSaleMock();

        sale1.setActive(false);
        sale2.setActive(false);

        saleRepository.insert(Arrays.asList(sale1, sale2));

        Assert.assertEquals(insertedProduct.getPrice(), productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenSalesAreTooOldTest() {
        LocalDate pastDate = LocalDate.now().minusDays(10);

        Sale sale = getDefaultSaleMock();
        sale.setStartDate(pastDate);
        sale.setEndDate(pastDate.plusDays(2));

        saleRepository.insert(sale);

        Assert.assertEquals(insertedProduct.getPrice(), productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenSalesDidNotStart() {
        Sale sale = getDefaultSaleMock();
        sale.setStartDate(LocalDate.now().plusDays(1));

        saleRepository.insert(sale);
        Assert.assertEquals(insertedProduct.getPrice(), productPriceProvider.getLowest(insertedProduct));
    }

    @Test
    public void lowestPriceWhenNominalValueAndPercentValueHigherThenProductPriceTest() {
        Sale sale = getDefaultSaleMock();
        sale.setPercentValue(101);
        sale.setNominalValue(insertedProduct.getPrice().add(BigDecimal.ONE));
        saleRepository.insert(sale);
        Assert.assertEquals(insertedProduct.getPrice(), productPriceProvider.getLowest(insertedProduct));
    }

    @After
    public void clean() {
        productRepository.deleteAll();
        saleRepository.deleteAll();
    }
}