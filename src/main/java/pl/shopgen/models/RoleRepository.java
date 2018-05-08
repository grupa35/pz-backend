package pl.shopgen.models;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, String> {
}
