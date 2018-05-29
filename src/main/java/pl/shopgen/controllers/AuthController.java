package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.services.AuthService;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController extends AbstractController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String register(@RequestBody RegistrationCredentialsDTO registrationCredentialsDTO) {
        Map<String, Integer> response = new HashMap<>();
        response.put("result", authService.register(registrationCredentialsDTO));
        return mapToJson(response);
    }
}
