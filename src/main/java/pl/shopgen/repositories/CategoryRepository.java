package pl.shopgen.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.shopgen.models.Category;

import java.util.Optional;

public interface CategoryRepository extends MongoRepository<Category, String> {
    Optional<Category> findByName(String name);
}
