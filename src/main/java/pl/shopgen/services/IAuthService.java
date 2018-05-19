package pl.shopgen.services;

import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.models.RegistrationStatusDTO;

public interface IAuthService {

    RegistrationStatusDTO register(RegistrationCredentialsDTO credentials);
}
