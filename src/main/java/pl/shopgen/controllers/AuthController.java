package pl.shopgen.controllers;


        import org.springframework.web.bind.annotation.RequestBody;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RestController;
        import pl.shopgen.models.RegistrationCredentialsDTO;

@RestController

public class AuthController {

    public AuthController() {
    }


    @RequestMapping(value = "/register",method = RequestMethod.POST)
    RegistrationCredentialsDTO register(@RequestBody RegistrationCredentialsDTO registrationCredentialsDTO){
        return registrationCredentialsDTO;
    }
}
