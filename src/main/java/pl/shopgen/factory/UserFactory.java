package pl.shopgen.factory;

import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.models.Role;
import pl.shopgen.models.User;

public class UserFactory {

    public static User createRegisteredUser(RegistrationCredentialsDTO credentials, Role role) {
        User user = new User();
        user.setEmail(credentials.getEmail());
        user.setEnabled(true);
        user.setName(credentials.getName());
        user.setPassword(credentials.getPassword());
        user.setRole(role);
        user.setSurname(credentials.getSurname());

        return user;
    }
}
