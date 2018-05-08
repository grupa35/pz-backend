package pl.shopgen.models.mocks;

import pl.shopgen.models.Role;
import pl.shopgen.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersGenerator {

    private List<User> users = new ArrayList<>();

    private Role role = new Role();

    private User u1, u2, u3, u4;

    public UsersGenerator() {
    }

    public List<User> generateUsers(){
        u1 = new User();
        u1.setName("Anna");
        u1.setSurname("Nowak");
        u1.setPassword("qwerty".toCharArray());
        u1.setAddress("ul.Stradomska 20, 31-514 Kraków");
        u1.setEmail("ania@gmail.com");
        role.setName("user");
        u1.setRole(role);

        u2 = new User();
        u2.setName("Marta");
        u2.setSurname("Kowalska");
        u2.setPassword("qwerty123".toCharArray());
        u2.setAddress("ul.Warszawska 24, 31-200 Poznań");
        u2.setEmail("martusia@gmail.com");
        u2.setRole(role);

        u3 = new User();
        u3.setName("Dawid");
        u3.setSurname("Wojciechowski");
        u3.setPassword("qwerty3455".toCharArray());
        u3.setAddress("ul.Michałkowska 123, 32-514 Warszawa");
        u3.setEmail("dejwid@gmail.com");
        u3.setRole(role);

        u4 = new User();
        u4.setName("Karol");
        u4.setSurname("Pociąg");
        u4.setPassword("1234".toCharArray());
        u4.setAddress("ul.Ladna 2, 31-514 Kraków");
        u4.setEmail("karol@gmail.com");
        u4.setRole(role);

        users.addAll(Arrays.asList(u1, u2, u3, u4));

        return users;
    }
}
