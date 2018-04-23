package pl.shopgen.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //categories - entity, string - type id
public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findById(String id);
    void deleteById(String id);
    void deleteAllById(Iterable<String> id);
}
