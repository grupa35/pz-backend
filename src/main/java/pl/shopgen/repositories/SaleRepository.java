package pl.shopgen.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.shopgen.models.Sale;

public interface SaleRepository extends MongoRepository<Sale, String> {
    List<Sale> findAllByProductId(String productId);
}
