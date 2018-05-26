package pl.shopgen.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import pl.shopgen.codes.RegistrationResultCode;
import pl.shopgen.factory.UserFactory;
import pl.shopgen.models.RegistrationCredentialsDTO;
import pl.shopgen.models.RegistrationStatusDTO;
import pl.shopgen.models.Role;
import pl.shopgen.repositories.RoleRepository;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AuthServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Autowired
    @Qualifier("BCryptPasswordEncoderComponent")
    private PasswordEncoder passwordEncoder;

    private AuthService authService;

    private String savedUserRoleName = "user";

    @Before
    public void setUp() {
        Mockito.when(roleRepository.findByName(Mockito.anyString()))
                .then(invocation -> {
                    if(invocation.getArgument(0).equals(savedUserRoleName)) {
                        return Optional.of(new Role(savedUserRoleName));
                    } else {
                        return Optional.empty();
                    }
                });
        Mockito.when(userRepository.save(Mockito.any(User.class))).then(invocation -> invocation.getArgument(0));
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .then(invocation -> {
                    if(invocation.getArgument(0).equals(getSavedUserMock().getEmail())) {
                        return Optional.of(getSavedUserMock());
                    } else {
                        return Optional.empty();
                    }
                });
        authService = new AuthService(userRepository, passwordEncoder, roleRepository);
    }

    private User getSavedUserMock() {
        User user = UserFactory.createRegisteredUser(getGoodRegistrationCredentialsMock(), new Role(savedUserRoleName));
        user.setEmail("savedUserTest@test.pl");
        return user;
    }

    private RegistrationCredentialsDTO getGoodRegistrationCredentialsMock() {
        RegistrationCredentialsDTO registrationCredentialsDTO = new RegistrationCredentialsDTO();
        registrationCredentialsDTO.setName("test");
        registrationCredentialsDTO.setSurname("surtest");
        registrationCredentialsDTO.setEmail("test@test.pl");
        registrationCredentialsDTO.setPassword("12345678a");
        registrationCredentialsDTO.setRePassword("12345678a");
        registrationCredentialsDTO
                .setRoleName(roleRepository.findByName(savedUserRoleName).map(Role::getName).orElse(""));
        return registrationCredentialsDTO;
    }

    @Test
    public void registrationWithWrongEmailFormatTest() {
        List<String> wrongEmails = new ArrayList<>(Arrays.asList(
                "test.pl",
                "test@test",
                "@test.pl",
                "test@test.",
                "@."
        ));


        RegistrationStatusDTO expectedStatus = new RegistrationStatusDTO(RegistrationResultCode.WRONG_EMAIL_FORMAT);
        RegistrationCredentialsDTO registrationCredentialsDTO = getGoodRegistrationCredentialsMock();

        for (String email : wrongEmails) {
            registrationCredentialsDTO.setEmail(email);
            Assert.assertEquals(expectedStatus, authService.register(registrationCredentialsDTO));
        }
    }

    @Test
    public void registrationSuccessTest() {
        RegistrationCredentialsDTO registrationCredentialsDTO = getGoodRegistrationCredentialsMock();
        RegistrationStatusDTO expectedStatus = new RegistrationStatusDTO(RegistrationResultCode.SUCCESS);

        Assert.assertEquals(expectedStatus, authService.register(registrationCredentialsDTO));
    }

    @Test
    public void registrationWithEmailWhichExistsTest() {
        RegistrationCredentialsDTO registrationCredentialsDTO = getGoodRegistrationCredentialsMock();
        RegistrationStatusDTO expectedStatus = new RegistrationStatusDTO(RegistrationResultCode.EMAIL_EXISTS);
        registrationCredentialsDTO.setEmail(getSavedUserMock().getEmail());
        Assert.assertEquals(expectedStatus, authService.register(registrationCredentialsDTO));

    }

    @Test
    public void registrationWithWrongPasswordFormat() {
        List<String> wrongPasswords = new ArrayList<>(Arrays.asList(
                "1234567", // too short expected 8
                "12345678", // expected 1 letter
                "aaaaaaaa" // expected 1 number
        ));
        RegistrationStatusDTO expectedStatus = new RegistrationStatusDTO(RegistrationResultCode.WRONG_PASSWORD_FORMAT);
        RegistrationCredentialsDTO registrationCredentialsDTO = getGoodRegistrationCredentialsMock();

        for (String pass : wrongPasswords) {
            registrationCredentialsDTO.setPassword(pass);
            registrationCredentialsDTO.setRePassword(pass);
            Assert.assertEquals(expectedStatus, authService.register(registrationCredentialsDTO));
        }
    }

    @Test
    public void registrationWithDifferentPasswords() {
        RegistrationCredentialsDTO registrationCredentialsDTO = getGoodRegistrationCredentialsMock();
        registrationCredentialsDTO.setPassword("different123");

        RegistrationStatusDTO expectedStatus = new RegistrationStatusDTO(RegistrationResultCode.DIFFERENT_PASSWORDS);
        Assert.assertEquals(expectedStatus, authService.register(registrationCredentialsDTO));
    }

    @Test
    public void registrationWithRoleWhichNotExits() {
        RegistrationCredentialsDTO registrationCredentialsDTO = getGoodRegistrationCredentialsMock();
        registrationCredentialsDTO.setRoleName("2341sdfsf");

        RegistrationStatusDTO expectedStatus = new RegistrationStatusDTO(RegistrationResultCode.WRONG_ROLE);
        Assert.assertEquals(expectedStatus, authService.register(registrationCredentialsDTO));
    }
}