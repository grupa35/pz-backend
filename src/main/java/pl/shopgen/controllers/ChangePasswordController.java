package pl.shopgen.controllers;


import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.NewPasswordDTO;
import pl.shopgen.models.ResponseStatusDTO;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;
import pl.shopgen.services.UserService;
import pl.shopgen.validator.RegexPattern;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class ChangePasswordController extends AbstractController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;

    ChangePasswordController(UserRepository userRepository, UserService userService, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.encoder = encoder;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(@RequestBody NewPasswordDTO newPasswordDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> userOptional = userRepository.findByEmail(username);
        if(!userOptional.isPresent()) {
            throw new UsernameNotFoundException(username);
        }
        User user = userOptional.get();
        RegexValidator passwordValidator = new RegexValidator(RegexPattern.PASSWORD);
        if(!newPasswordDTO.getNewPassword().equals(newPasswordDTO.getReNewPassword())) {
            return mapToJson(new ResponseStatusDTO(10, "Your password and confirmation password do not match."));
        } else if(!newPasswordDTO.getOldPassword().equals(user.getPassword())) {
            return mapToJson(new ResponseStatusDTO(12, "Old password is incorrect."));
        } else if(!passwordValidator.isValid(newPasswordDTO.getNewPassword())) {
            return mapToJson(new ResponseStatusDTO(11, "New password doesn't meet the minimum security requirements."));
        } else {
            user.setPassword(encoder.encode(newPasswordDTO.getNewPassword()));
            userService.updateUser(user);
            return mapToJson(new ResponseStatusDTO(0, "Password successfully changed."));
        }
    }
}
