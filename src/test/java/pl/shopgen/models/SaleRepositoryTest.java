package pl.shopgen.models;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.shopgen.models.mocks.ProductListGenerator;
import pl.shopgen.models.mocks.SalesGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class SaleRepositoryTest extends SimpleMongoRepositoryTest<Sale, SaleRepository> {

    @Autowired
    private ProductRepository productRepository;

    private Product insertedProduct;

    private SalesGenerator salesGenerator;

    private Random random = new Random();

    @Before
    public void init() {
        insertProduct();
        salesGenerator = new SalesGenerator(productRepository);
    }

    public void insertProduct() {
        insertedProduct = productRepository.insert(getProduct());
    }

    private Product getProduct() {
        ProductListGenerator productListGenerator = new ProductListGenerator();
        Product product = productListGenerator.generateProducts().get(0);
        product.setPrice(new BigDecimal(1000.0));

        return product;
    }

    @Test
    public void findAllByProductId() {

        List<Sale> insertedSales = new ArrayList<>(repository.insert(getObjects()));

        List<Sale> foundedSales = repository.findAllByProductId(insertedProduct.getId());

        Assert.assertEquals(insertedSales, foundedSales);
    }

    @Override
    public Sale getObject() {
        return salesGenerator.generateSale(insertedProduct.getId());
    }


    @Override
    public Sale getChangedObject(Sale object) {
        Sale objectToChange = new Sale(object);
        objectToChange.setPercentValue(10);
        objectToChange.setNominalValue(BigDecimal.TEN);
        objectToChange.setProductId(insertedProduct.getId());
        objectToChange.setEndDate(LocalDate.now().plusDays(100));
        objectToChange.setStartDate(LocalDate.now().minusDays(10));
        objectToChange.setActive(!object.isActive());
        objectToChange.setCode("987654321");
        return objectToChange;
    }


    @Override
    public List<Sale> getObjects() {
        List<Sale> objects = new ArrayList<>();
        objects.add(getObject());
        objects.add(getObject());
        objects.add(getObject());

        return objects;
    }


    @After
    @Override
    public void clean() {
        super.clean();
        productRepository.deleteAll();
    }
}