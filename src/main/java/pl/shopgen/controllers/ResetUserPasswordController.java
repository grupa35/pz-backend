package pl.shopgen.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.PasswordResetDto;
import pl.shopgen.models.RandomPasswordGenerator;
import pl.shopgen.models.User;
import pl.shopgen.models.UserRepository;
import pl.shopgen.services.EmailService;
import pl.shopgen.services.UserService;

import java.util.Optional;

@RequestMapping("/users")
@RestController
public class ResetUserPasswordController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserService userService;
    private final EmailService emailService;
    private final RandomPasswordGenerator passwordGenerator;

    public ResetUserPasswordController(UserRepository userRepository, BCryptPasswordEncoder encoder, UserService userService, EmailService emailService, RandomPasswordGenerator passwordGenerator) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordGenerator = passwordGenerator;
    }

    @GetMapping("/resetpwd/{email}")
    public PasswordResetDto resetUserPwd(@PathVariable String email)
    {

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent())
        {
            return new PasswordResetDto(false, "Nie istnieje user o podanym mejlu");
        }
        /*Generowanie hasła*/

        String newPwd = passwordGenerator.generatePassword(8);

        User user = userOptional.get();
        user.setPassword(encoder.encode(newPwd));
        userService.updateUser(user);

        emailService.sendSimpleMessage(email,"Password reset",newPwd);

        return new PasswordResetDto(true, "Pomyślnie zmieniono hasło");
    }
}
