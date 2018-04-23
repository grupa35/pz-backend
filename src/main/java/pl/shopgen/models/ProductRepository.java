package pl.shopgen.models;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findById(String id);
    void deleteById(String id);
    void deleteAllById(Iterable<String> id);

}
