package pl.shopgen.services;

import pl.shopgen.models.RegistrationCredentialsDTO;

public interface IAuthService {

    Integer register(RegistrationCredentialsDTO credentials);
}
