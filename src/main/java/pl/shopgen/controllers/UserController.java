package pl.shopgen.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(method = RequestMethod.POST)
    User addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.insert(user);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    List<User> deleteUsers() {
        List<User> users = userRepository.findAll();
        userRepository.deleteAll();
        return users;
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    User deleteUser(@PathVariable("userId") String userId) {

        User user = userRepository.findById(userId).orElse(null);

        if(user != null) {
           userRepository.deleteById(userId);
        }

        return user;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<User> getUsers() {
        return userRepository.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ResponseBody
    User getUser(@PathVariable("userId") String userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @RequestMapping(method = RequestMethod.PUT)
    User updateUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
