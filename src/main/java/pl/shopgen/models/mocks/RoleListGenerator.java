package pl.shopgen.models.mocks;

import pl.shopgen.models.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoleListGenerator {

    private List<Role> roles = new ArrayList<>();

    private Role r1, r2;

    RoleListGenerator() {
    }

    public List<Role> generateRoles() {
        /* Rola 1 */
        r1 = new Role();
        r1.setName("Administator");

        /* Rola 2 */
        r2 = new Role();
        r2.setName("Użytkownik");

        /* Fulfill list */
        roles.addAll(Arrays.asList(r1, r2));

        /* Return list of products */
        return roles;

    }
}
