package pl.shopgen.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String deleteUsers() {
        String userJson = mapToJson(userRepository.findAll());
        userRepository.deleteAll();
        return userJson;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public String deleteUser(@PathVariable("userId") String userId) {

        String userJson = mapToJson(userRepository.findById(userId).orElse(null));

        if(userJson.isEmpty()) {
           userRepository.deleteById(userId);
        }

        return userJson;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String getUser(@PathVariable("userId") String userId) {
        return mapToJson(userRepository.findById(userId).orElse(null));
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getUsers() {
        return mapToJson(userRepository.findAll());
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return mapToJson(userRepository.findByEmail(email).orElse(null));
    }
}
