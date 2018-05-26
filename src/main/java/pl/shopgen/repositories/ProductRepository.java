package pl.shopgen.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.shopgen.models.Product;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
}
