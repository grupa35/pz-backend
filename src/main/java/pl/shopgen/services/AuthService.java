package pl.shopgen.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.shopgen.codes.RegistrationResultCode;
import pl.shopgen.factory.UserFactory;
import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.models.Role;
import pl.shopgen.models.User;
import pl.shopgen.repositories.RoleRepository;
import pl.shopgen.repositories.UserRepository;
import pl.shopgen.validator.RegistrationValidator;

@Service
public class AuthService implements IAuthService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;


    public AuthService(UserRepository userRepository,
            @Qualifier("BCryptPasswordEncoderComponent") PasswordEncoder passwordEncoder,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public Integer register(RegistrationCredentialsDTO credentials) {

        Integer registrationStatus = RegistrationValidator
                .getInstance(credentials, roleRepository, userRepository)
                .addCheckEmailFormat()
                .addCheckEmailExists()
                .addCheckPasswordsEqual()
                .addCheckPasswordFormat()
                .addCheckRole()
                .validate();

        if(registrationStatus == null) {
            credentials.setPassword(passwordEncoder.encode(credentials.getPassword()));
            User registeredUser = UserFactory
                    .createRegisteredUser(credentials, getRoleByName(credentials.getRoleName()));
            User savedUser = userRepository.save(registeredUser);
            if(savedUser != null) {
                registrationStatus = RegistrationResultCode.SUCCESS;
            } else {
                registrationStatus = RegistrationResultCode.SAVE_ERROR;
            }
        }

        return registrationStatus;
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName).orElse(null);
    }
}