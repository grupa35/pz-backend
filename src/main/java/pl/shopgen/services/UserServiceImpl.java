package pl.shopgen.services;

import org.springframework.stereotype.Service;
import pl.shopgen.models.User;
import pl.shopgen.models.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
