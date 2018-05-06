package pl.shopgen.models.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.shopgen.models.Role;
import pl.shopgen.models.RoleRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleDbSeeder implements CommandLineRunner {
    private RoleRepository roleRepository;

    public RoleDbSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... strings) {

        Role administrator = new Role("administrator");
        Role user = new Role("użytkownik");


        //  drop all
        roleRepository.deleteAll();

        // add data to the database
        List<Role> role = Arrays.asList(administrator, user);
        for (Role data : role) {
            roleRepository.save(data);
        }
    }
}
