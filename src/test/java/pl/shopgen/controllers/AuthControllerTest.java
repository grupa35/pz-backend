package pl.shopgen.controllers;

import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.ShopApplication;
import pl.shopgen.codes.RegistrationResultCode;
import pl.shopgen.models.Role;
import pl.shopgen.models.User;
import pl.shopgen.repositories.RoleRepository;
import pl.shopgen.repositories.UserRepository;
import pl.shopgen.security.BCryptPasswordEncoderComponent;
import pl.shopgen.services.AuthService;

import java.util.Optional;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = AuthController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration(classes = {ShopApplication.class, AuthService.class, BCryptPasswordEncoderComponent.class})
@WebAppConfiguration
public class AuthControllerTest {

    private String userIdInDatabase;

    private String userIdNotInDatabase;

    private String emailNotInDatabase;

    private String roleIdInDatabase;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        userIdInDatabase = new ObjectId().toString();
        userIdNotInDatabase = new ObjectId().toString();
        roleIdInDatabase = new ObjectId().toString();
        emailNotInDatabase = "test@test.pl";

        Mockito.when(roleRepository.findByName(Mockito.anyString())).then(invocation -> {
            Role role = new Role();
            role.setId(roleIdInDatabase);
            role.setName(invocation.getArgument(0));
            return Optional.of(role);
        });

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).then(invocation -> {
            if(invocation.getArgument(0).equals(emailNotInDatabase)) {
                return Optional.empty();
            } else {
                User user = getUserMockWithoutId(invocation.getArgument(0));
                user.setId(userIdInDatabase);
                return Optional.of(user);
            }
        });

        Mockito.when(userRepository.save(Mockito.any(User.class))).then(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(new ObjectId().toString());
            return user;
        });
    }

    private User getUserMockWithoutId(String email) {
        User user = new User();
        user.setName("John");
        user.setSurname("Smith");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode("12345678a"));
        user.setEmail(email);
        Role role = new Role("customer");
        role.setId(roleIdInDatabase);
        user.setRole(role);
        return user;
    }

    @Test
    public void registrationOkTest() throws Exception {
        String requestContent = new JSONObject()
                .put("name", "John")
                .put("surname", "Smith")
                .put("email", "test@test.pl")
                .put("roleName", "customer")
                .put("password", "12345678a")
                .put("rePassword", "12345678a")
                .toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(RegistrationResultCode.SUCCESS))
                .andDo(MockMvcRestDocumentation
                        .document("auth/registration/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.requestFields(
                                        PayloadDocumentation.fieldWithPath("name").description("User name"),
                                        PayloadDocumentation.fieldWithPath("surname").description("User surname"),
                                        PayloadDocumentation.fieldWithPath("email").description("User email"),
                                        PayloadDocumentation.fieldWithPath("roleName").description(
                                                "Depends on registration view. When is normal user should be \"customer\""),
                                        PayloadDocumentation.fieldWithPath("password")
                                                .description("Minimum: length 8, 1 letter, 1 number"),
                                        PayloadDocumentation.fieldWithPath("rePassword")
                                                .description("Repeated passowrd")
                                ),
                                PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("result")
                                        .description(
                                                "0 means that registration succeed. Other numbers define errors - details in documentation"))));
    }
}