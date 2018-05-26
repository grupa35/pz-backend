package pl.shopgen.models;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.shopgen.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryTest extends SimpleMongoRepositoryTest<Product, ProductRepository> {

    @Override
    public Product getObject() {
        Product product = new Product();
        product.setCategory(new Category("buty",
                                         new ArrayList<>(Collections.singletonList(new Category("meskie",
                                                                                                new ArrayList<>())))));
        product.setImgUrl("img-url");
        product.setName("Reebok");
        product.setPrice(new BigDecimal(100.2));
        product.getSizeToAmountMap().put("40", 20);
        product.getSizeToAmountMap().put("41", 30);
        product.getSizeToAmountMap().put("42", 40);

        Description description = new Description();
        description.setName("zalety");
        description.setValue("najlepsze buty");
        product.setDescription(description);
        return product;
    }

    @Override
    public List<Product> getObjects() {
        List<Product> products = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> {
            Product product = getObject();
            product.setName(product.getName() + i);
            products.add(product);
        });

        return products;
    }

    @Override
    public Product getChangedObject(Product object) {
        Product changedObject = new Product(object);
        changedObject.setName(changedObject.getName() + "changedName");
        return changedObject;
    }
}
