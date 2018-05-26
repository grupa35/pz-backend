package pl.shopgen.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.shopgen.models.Description;

import java.util.List;

public interface DescriptionRepository extends MongoRepository<Description, String> {
}
