package pl.shopgen.controllers;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.shopgen.models.ResponseStatusDTO;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;
import pl.shopgen.services.UserService;

import java.util.Map;

@RequestMapping("/user/setEmail")
@RestController
public class ChangeUserEmailController extends AbstractController {

    private final UserRepository userRepository;
    private final UserService userService;


    public ChangeUserEmailController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping()
    public String setUserEmail(@RequestBody Map<String,String> json){
        String email = json.get("email");

        if(!EmailValidator.getInstance().isValid(email)){
           return mapToJson(new ResponseStatusDTO(10,"Incorrect email"));
        }
        else if(userRepository.findByEmail(email).isPresent()){
           return mapToJson(new ResponseStatusDTO(20,"Email already exists"));
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName()).get();
        user.setEmail(email);

        userService.updateUser(user);

        return mapToJson(new ResponseStatusDTO(0,"OK"));
    }

}
