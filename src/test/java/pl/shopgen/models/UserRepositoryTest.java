package pl.shopgen.models;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.shopgen.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest extends SimpleMongoRepositoryTest<User, UserRepository> {

        @Override
        public User getObject() {
            User user = new User();
            user.setName("Anna");
            user.setSurname("Kowalska");
            user.setPassword("hjryiiuf");
            user.setEmail("anna@gmail.com");
            user.setRole(new Role("uzytkownik"));
            return user;
        }

        @Override
        public List<User> getObjects() {
            List<User> users = new ArrayList<>();
            IntStream.range(0, 5).forEach(i -> {
                User user = getObject();
                user.setName(user.getName() + i);
                users.add(user);
            });
            return users;
        }

        @Override
        public User getChangedObject(User user) {
            User newUser = new User(user);
            newUser.setSurname(newUser.getSurname() + "-Nowak");
            return newUser;
        }
}
