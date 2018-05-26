package pl.shopgen.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.shopgen.models.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
