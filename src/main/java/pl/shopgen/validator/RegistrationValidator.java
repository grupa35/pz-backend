package pl.shopgen.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import pl.shopgen.codes.RegistrationResultCode;
import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.models.RegistrationStatusDTO;
import pl.shopgen.repositories.RoleRepository;
import pl.shopgen.repositories.UserRepository;

public class RegistrationValidator {

    RegistrationStatusDTO registrationStatusDTO;

    RoleRepository roleRepository;

    UserRepository userRepository;

    private RegistrationCredentialsDTO credentials;

    private RegistrationValidator(RegistrationCredentialsDTO credentials, RoleRepository roleRepository, UserRepository userRepository) {
        this.credentials = credentials;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public static RegistrationValidator getInstance(RegistrationCredentialsDTO credentials, RoleRepository roleRepository, UserRepository userRepository) {
        return new RegistrationValidator(credentials, roleRepository, userRepository);
    }

    public RegistrationValidator addCheckEmailExists() {
        if(registrationStatusDTO == null && userRepository.findByEmail(credentials.getEmail()).isPresent()) {
            registrationStatusDTO = new RegistrationStatusDTO(RegistrationResultCode.EMAIL_EXISTS);
        }
        return this;
    }

    public RegistrationValidator addCheckEmailFormat() {
        if(registrationStatusDTO == null && !EmailValidator.getInstance().isValid(credentials.getEmail())) {
            registrationStatusDTO = new RegistrationStatusDTO(RegistrationResultCode.WRONG_EMAIL_FORMAT);
        }
        return this;
    }

    public RegistrationValidator addCheckPasswordFormat() {
        RegexValidator passwordValidator = new RegexValidator(RegexPattern.PASSWORD);
        if(registrationStatusDTO == null && !passwordValidator.isValid(credentials.getPassword())) {
            registrationStatusDTO = new RegistrationStatusDTO(RegistrationResultCode.WRONG_PASSWORD_FORMAT);
        }
        return this;
    }

    public RegistrationValidator addCheckPasswordsEqual() {
        if(registrationStatusDTO == null && !credentials.getPassword().equals(credentials.getRePassword())) {
            registrationStatusDTO = new RegistrationStatusDTO(RegistrationResultCode.DIFFERENT_PASSWORDS);
        }
        return this;
    }

    public RegistrationValidator addCheckRole() {
        if(registrationStatusDTO == null && !roleRepository.findByName(credentials.getRoleName()).isPresent()) {
            registrationStatusDTO = new RegistrationStatusDTO(RegistrationResultCode.WRONG_ROLE);
        }

        return this;
    }

    public RegistrationStatusDTO validate() {
        return registrationStatusDTO;
    }
}
