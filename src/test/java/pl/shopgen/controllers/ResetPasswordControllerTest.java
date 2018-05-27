package pl.shopgen.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.models.*;
import pl.shopgen.repositories.UserRepository;
import pl.shopgen.services.EmailService;
import pl.shopgen.services.UserService;

import java.util.Optional;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ResetUserPasswordController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration
@WebAppConfiguration
public class ResetPasswordControllerTest {
    private static final String USER_EMAIL = "justyna.pietryga@gmail.com";
    private static final String USER_EMAIL_NOT_FOUND = "noemail@gmail.com";
    private static final String USER_ID = "5b032c9efea5880b90fd789f";

    @MockBean
    UserRepository userRepository;
    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @MockBean
    UserService userService;
    @MockBean
    EmailService emailService;
    @MockBean
    RandomPasswordGenerator randomPasswordGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .then(invocation -> {
                    if(invocation.getArgument(0).equals(USER_EMAIL_NOT_FOUND)) {
                        return Optional.empty();
                    } else {
                        return createUserDatabaseMock(invocation.getArgument(0));
                    }
                });

        Mockito.when(userRepository.save(Mockito.any(User.class))).then(invocation -> invocation.getArgument(0));
    }

    private Optional<User> createUserDatabaseMock(String email) {
        User user = new User();
        user.setId(USER_ID);
        user.setName("Ania");
        user.setSurname("Kowalska");
        user.setEmail(email);
        user.setPassword("losowejhi54646kkjhuil");
        user.setRole(new Role("customer"));
        return Optional.of(user);
    }

    @Test
    public void passwordResetOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/resetpwd/" + USER_EMAIL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Pomyślnie zmieniono hasło"))
                .andDo(MockMvcRestDocumentation
                        .document("/resetPsw/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(PasswordResetDto.fieldsDescriptor())));
    }

//    @Test
//    public void passwordResetFailTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/users/resetpwd/" + USER_EMAIL_NOT_FOUND))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("Nie istnieje user o podanym mejlu"))
//                .andDo(MockMvcRestDocumentation
//                .document("/resetPsw/ok", preprocessResponse(prettyPrint()),
//                        PayloadDocumentation.responseFields(RoleDto.fieldsDescriptor())));
//    }
}
