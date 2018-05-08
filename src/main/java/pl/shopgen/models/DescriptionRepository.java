package pl.shopgen.models;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DescriptionRepository extends MongoRepository<Description, String> {
}
