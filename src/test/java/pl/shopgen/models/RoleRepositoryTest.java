package pl.shopgen.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class RoleRepositoryTest extends SimpleMongoRepositoryTest<Role, RoleRepository> {

    @Override
    public Role getObject() {
        Role role = new Role();
        role.setName("administrator");
        return role;
    }

    @Override
    public List<Role> getObjects() {
        List<Role> roles = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> {
            Role role = getObject();
            role.setName(role.getName() + i);
            roles.add(role);
        });
        return roles;
    }

    @Override
    public Role getChangedObject(Role object) {
        Role newRole = new Role("hydraulik");
        newRole.setName(newRole.getName() + "nowa");
        return newRole;
    }


}
