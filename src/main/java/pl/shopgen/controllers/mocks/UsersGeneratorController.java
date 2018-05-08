package pl.shopgen.controllers.mocks;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.Product;
import pl.shopgen.models.ProductRepository;
import pl.shopgen.models.User;
import pl.shopgen.models.UserRepository;
import pl.shopgen.models.mocks.ProductListGenerator;
import pl.shopgen.models.mocks.UsersGenerator;

import java.util.List;

@RestController
@RequestMapping("/mocks/usersGenerator")
public class UsersGeneratorController {

    private final UsersGenerator usersGenerator;

    private final UserRepository userRepository;

    public UsersGeneratorController(UserRepository userRepository) {
        this.userRepository = userRepository;
        usersGenerator = new UsersGenerator();
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<User> generateProducts() {
        return userRepository.insert(usersGenerator.generateUsers());
    }
}





