package pl.shopgen.provider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.shopgen.repositories.CategoryRepository;
import pl.shopgen.models.Product;
import pl.shopgen.repositories.ProductRepository;
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleDTO;
import pl.shopgen.repositories.SaleRepository;
import pl.shopgen.models.SaleType;
import pl.shopgen.models.mocks.ProductListGenerator;
import pl.shopgen.services.IProductSaleService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductSaleServiceTest {

    private Product insertedProduct;

    @Autowired
    private IProductSaleService productSaleService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Before
    public void init() {
        initProduct();
    }

    private void initProduct() {
        ProductListGenerator productListGenerator = new ProductListGenerator(categoryRepository);
        insertedProduct = productRepository.insert(productListGenerator.generateProducts().get(0));
    }

    @Test
    public void lowestPriceWhenNominalSaleTest() {
        BigDecimal saleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.NOMINAL);
        sale.setNominalValue(saleValue);
        saleRepository.insert(sale);

        Assert.assertEquals(insertedProduct.getPrice().subtract(saleValue),
                productSaleService.getLowestProductPrice(insertedProduct));

    }

    private Sale getDefaultSaleMock() {
        Sale sale = new Sale();
        sale.setPercentValue(25);
        sale.setNominalValue(insertedProduct.getPrice()
                .divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN)
                .setScale(2, RoundingMode.HALF_EVEN));
        sale.setStartDate(LocalDate.now());
        sale.setEndDate(LocalDate.now().plusDays(7));
        sale.setActive(true);
        sale.setCode("xyz");
        sale.setProductId(insertedProduct.getId());
        sale.setSaleType(SaleType.NOMINAL);

        return sale;
    }

    @Test
    public void lowestPriceWhenPercentSaleTest() {
        double percentValue = 20;
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.PERCENT);
        sale.setPercentValue(percentValue);

        BigDecimal saleValue = insertedProduct.getPrice().multiply(BigDecimal.valueOf(percentValue / 100));
        saleRepository.insert(sale);

        Assert.assertEquals(insertedProduct.getPrice().subtract(saleValue),
                productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void lowestPriceWhenNoSaleTest() {
        Assert.assertEquals(insertedProduct.getPrice(), productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void lowestPriceWhenManySalesAndNominalIsHigherTest() {
        double percentSaleValue = 20;
        BigDecimal nominalSaleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        Assert.assertEquals(insertedProduct.getPrice().subtract(nominalSaleValue),
                productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void lowestPriceWhenNominalSaleAndPercentSaleHigherThanProductPriceTest() {
        double percentSaleValue = 101;
        BigDecimal nominalSaleValue = insertedProduct.getPrice()
                .multiply(BigDecimal.valueOf(1.01));

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        Assert.assertEquals(insertedProduct.getPrice(),
                productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void lowestPriceWhenAllSalesAreNotActiveTest() {
        Sale sale1 = getDefaultSaleMock();
        Sale sale2 = getDefaultSaleMock();

        sale1.setActive(false);
        sale2.setActive(false);

        saleRepository.insert(Arrays.asList(sale1, sale2));

        Assert.assertEquals(insertedProduct.getPrice(), productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void lowestPriceWhenSalesAreTooOldTest() {
        LocalDate pastDate = LocalDate.now().minusDays(10);

        Sale sale = getDefaultSaleMock();
        sale.setStartDate(pastDate);
        sale.setEndDate(pastDate.plusDays(2));

        saleRepository.insert(sale);

        Assert.assertEquals(insertedProduct.getPrice(), productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void lowestPriceWhenSalesDidNotStart() {
        Sale sale = getDefaultSaleMock();
        sale.setStartDate(LocalDate.now().plusDays(1));

        saleRepository.insert(sale);
        Assert.assertEquals(insertedProduct.getPrice(), productSaleService.getLowestProductPrice(insertedProduct));
    }

    @Test
    public void saleValueWhenNominalSaleTest() {
        BigDecimal saleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.NOMINAL);
        sale.setNominalValue(saleValue);
        saleRepository.insert(sale);

        Assert.assertEquals(saleValue, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenPercentSaleTest() {
        double percentValue = 20;
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.PERCENT);
        sale.setPercentValue(percentValue);

        BigDecimal saleValue = insertedProduct.getPrice().multiply(BigDecimal.valueOf(percentValue / 100));
        saleRepository.insert(sale);

        Assert.assertEquals(saleValue, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenNoSaleShouldBeZeroTest() {
        Assert.assertEquals(BigDecimal.ZERO, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenManySalesAndHigherIsNominalTest() {
        double percentSaleValue = 20;
        BigDecimal nominalSaleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        Assert.assertEquals(nominalSaleValue, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenNominalSaleAndPercentSaleHigherThanProductPriceShouldBeZeroTest() {
        double percentSaleValue = 101;
        BigDecimal nominalSaleValue = insertedProduct.getPrice()
                .multiply(BigDecimal.valueOf(1.01));

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        Assert.assertEquals(BigDecimal.ZERO, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenAllSalesAreNotActiveShouldBeZeroTest() {
        Sale sale1 = getDefaultSaleMock();
        Sale sale2 = getDefaultSaleMock();

        sale1.setActive(false);
        sale2.setActive(false);

        saleRepository.insert(Arrays.asList(sale1, sale2));

        Assert.assertEquals(BigDecimal.ZERO, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenSalesAreTooOldShouldBeZeroTest() {
        LocalDate pastDate = LocalDate.now().minusDays(10);

        Sale sale = getDefaultSaleMock();
        sale.setStartDate(pastDate);
        sale.setEndDate(pastDate.plusDays(2));

        saleRepository.insert(sale);

        Assert.assertEquals(BigDecimal.ZERO, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void saleValueWhenSalesDidNotStartShouldBeZeroTest() {
        Sale sale = getDefaultSaleMock();
        sale.setStartDate(LocalDate.now().plusDays(1));

        saleRepository.insert(sale);
        Assert.assertEquals(BigDecimal.ZERO, productSaleService.getSaleValue(insertedProduct));
    }

    @Test
    public void biggestSaleWhenNominalSaleTest() {
        BigDecimal saleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.NOMINAL);
        sale.setNominalValue(saleValue);
        saleRepository.insert(sale);

        Assert.assertEquals(sale, productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenPercentSaleTest() {
        double percentValue = 20;
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.PERCENT);
        sale.setPercentValue(percentValue);

        saleRepository.insert(sale);

        Assert.assertEquals(sale, productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenNoSalesShouldBeNullTest() {
        Assert.assertNull(productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenManySalesAndNominalIsHigherTest() {
        double percentSaleValue = 20;
        BigDecimal nominalSaleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        Assert.assertEquals(nominalSale, productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenNominalSaleAndPercentSaleHigherThanProductPriceShouldBeNullTest() {
        double percentSaleValue = 101;
        BigDecimal nominalSaleValue = insertedProduct.getPrice()
                .multiply(BigDecimal.valueOf(1.01));

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        Assert.assertNull(productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenAllSalesAreNotActiveShouldBeNullTest() {
        Sale sale1 = getDefaultSaleMock();
        Sale sale2 = getDefaultSaleMock();

        sale1.setActive(false);
        sale2.setActive(false);

        saleRepository.insert(Arrays.asList(sale1, sale2));

        Assert.assertNull(productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenSalesAreTooOldTest() {
        LocalDate pastDate = LocalDate.now().minusDays(10);

        Sale sale = getDefaultSaleMock();
        sale.setStartDate(pastDate);
        sale.setEndDate(pastDate.plusDays(2));

        saleRepository.insert(sale);

        Assert.assertNull(productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void biggestSaleWhenSalesDidNotStartShouldBeNullTest() {
        Sale sale = getDefaultSaleMock();
        sale.setStartDate(LocalDate.now().plusDays(1));

        saleRepository.insert(sale);
        Assert.assertNull(productSaleService.getBiggestSale(insertedProduct));
    }

    @Test
    public void saleDtoWhenNominalSaleTest() {
        BigDecimal saleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.NOMINAL);
        sale.setNominalValue(saleValue);
        saleRepository.insert(sale);

        SaleDTO saleDTO = SaleDTO.builder()
                .saleValue(saleValue)
                .priceAfterSale(insertedProduct.getPrice().subtract(saleValue))
                .salePercentValue(sale.getPercentValue())
                .saleNominalValue(saleValue)
                .code(sale.getCode())
                .isSale(true)
                .saleType(SaleType.NOMINAL)
                .build();

        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenPercentSaleTest() {
        double percentValue = 20;
        Sale sale = getDefaultSaleMock();
        sale.setSaleType(SaleType.PERCENT);
        sale.setPercentValue(percentValue);

        BigDecimal saleValue = insertedProduct.getPrice().multiply(BigDecimal.valueOf(percentValue / 100));
        saleRepository.insert(sale);

        SaleDTO saleDTO = SaleDTO.builder()
                .saleValue(saleValue)
                .priceAfterSale(insertedProduct.getPrice().subtract(saleValue))
                .salePercentValue(percentValue)
                .saleNominalValue(sale.getNominalValue())
                .code(sale.getCode())
                .isSale(true)
                .saleType(SaleType.PERCENT)
                .build();

        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenManySalesAndNominalHigherTest() {
        double percentSaleValue = 20;
        BigDecimal nominalSaleValue = insertedProduct.getPrice().divide(BigDecimal.valueOf(3), RoundingMode.HALF_EVEN);

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        SaleDTO saleDTO = SaleDTO.builder()
                .saleValue(nominalSaleValue)
                .priceAfterSale(insertedProduct.getPrice().subtract(nominalSaleValue))
                .salePercentValue(nominalSale.getPercentValue())
                .saleNominalValue(nominalSaleValue)
                .code(nominalSale.getCode())
                .isSale(true)
                .saleType(SaleType.NOMINAL)
                .build();

        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenNoSalesTest() {
        SaleDTO saleDTO = SaleDTO.builder().isSale(false).build();
        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenNominalSaleAndPercentSaleHigherThanProductPriceTest() {
        double percentSaleValue = 101;
        BigDecimal nominalSaleValue = insertedProduct.getPrice()
                .multiply(BigDecimal.valueOf(1.01));

        Sale percentSale = getDefaultSaleMock();
        percentSale.setSaleType(SaleType.PERCENT);
        percentSale.setPercentValue(percentSaleValue);

        Sale nominalSale = getDefaultSaleMock();
        nominalSale.setSaleType(SaleType.NOMINAL);
        nominalSale.setNominalValue(nominalSaleValue);


        saleRepository.insert(percentSale);
        saleRepository.insert(nominalSale);

        SaleDTO saleDTO = SaleDTO.builder().isSale(false).build();
        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenAllSalesAreNotActiveTest() {
        Sale sale1 = getDefaultSaleMock();
        Sale sale2 = getDefaultSaleMock();

        sale1.setActive(false);
        sale2.setActive(false);


        saleRepository.insert(Arrays.asList(sale1, sale2));
        SaleDTO saleDTO = SaleDTO.builder().isSale(false).build();
        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenSalesAreTooOldTest() {
        LocalDate pastDate = LocalDate.now().minusDays(10);

        Sale sale = getDefaultSaleMock();
        sale.setStartDate(pastDate);
        sale.setEndDate(pastDate.plusDays(2));

        saleRepository.insert(sale);
        SaleDTO saleDTO = SaleDTO.builder().isSale(false).build();
        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @Test
    public void saleDtoWhenSalesDidNotStart() {
        Sale sale = getDefaultSaleMock();
        sale.setStartDate(LocalDate.now().plusDays(1));

        saleRepository.insert(sale);
        SaleDTO saleDTO = SaleDTO.builder().isSale(false).build();
        Assert.assertEquals(saleDTO, productSaleService.getSaleDTO(insertedProduct));
    }

    @After
    public void clean() {
        productRepository.deleteAll();
        saleRepository.deleteAll();
    }
}