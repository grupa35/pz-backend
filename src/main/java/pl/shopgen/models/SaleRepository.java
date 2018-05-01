package pl.shopgen.models;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SaleRepository extends MongoRepository<Sale, String> {
    List<Sale> findAllByProductId(String productId);
}
