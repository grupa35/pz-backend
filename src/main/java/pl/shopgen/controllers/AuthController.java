package pl.shopgen.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.models.RegistrationStatusDTO;
import pl.shopgen.services.AuthService;

@RestController

public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public RegistrationStatusDTO register(@RequestBody RegistrationCredentialsDTO registrationCredentialsDTO) {
        return authService.register(registrationCredentialsDTO);
    }
}
