package pl.shopgen.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.RegexValidator;
import pl.shopgen.codes.RegistrationResultCode;
import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.repositories.RoleRepository;
import pl.shopgen.repositories.UserRepository;

public class RegistrationValidator {

    Integer RegistrationStatus;

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
        if(RegistrationStatus == null && userRepository.findByEmail(credentials.getEmail()).isPresent()) {
            RegistrationStatus = RegistrationResultCode.EMAIL_EXISTS;
        }
        return this;
    }

    public RegistrationValidator addCheckEmailFormat() {
        if(RegistrationStatus == null && !EmailValidator.getInstance().isValid(credentials.getEmail())) {
            RegistrationStatus = RegistrationResultCode.WRONG_EMAIL_FORMAT;
        }
        return this;
    }

    public RegistrationValidator addCheckPasswordFormat() {
        RegexValidator passwordValidator = new RegexValidator(RegexPattern.PASSWORD);
        if(RegistrationStatus == null && !passwordValidator.isValid(credentials.getPassword())) {
            RegistrationStatus = RegistrationResultCode.WRONG_PASSWORD_FORMAT;
        }
        return this;
    }

    public RegistrationValidator addCheckPasswordsEqual() {
        if(RegistrationStatus == null && !credentials.getPassword().equals(credentials.getRePassword())) {
            RegistrationStatus = RegistrationResultCode.DIFFERENT_PASSWORDS;
        }
        return this;
    }

    public RegistrationValidator addCheckRole() {
        if(RegistrationStatus == null && !roleRepository.findByName(credentials.getRoleName()).isPresent()) {
            RegistrationStatus = RegistrationResultCode.WRONG_ROLE;
        }

        return this;
    }

    public Integer validate() {
        return RegistrationStatus;
    }
}
