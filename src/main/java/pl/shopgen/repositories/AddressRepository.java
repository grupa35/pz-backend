package pl.shopgen.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pl.shopgen.models.Address;
import pl.shopgen.models.Category;

public interface AddressRepository extends MongoRepository<Address, String> {
}
