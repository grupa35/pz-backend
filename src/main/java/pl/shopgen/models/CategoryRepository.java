package pl.shopgen.models;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository //categories - entity, string - type id
public interface CategoryRepository extends MongoRepository<Category, String> {

}
