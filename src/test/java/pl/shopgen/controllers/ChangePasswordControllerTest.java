package pl.shopgen.controllers;

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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import pl.shopgen.ShopApplication;
import pl.shopgen.models.Role;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;
import pl.shopgen.security.BCryptPasswordEncoderComponent;
import pl.shopgen.services.UserService;

import java.util.Optional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ChangePasswordController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration(classes = {ShopApplication.class, BCryptPasswordEncoderComponent.class})
@WebAppConfiguration
public class ChangePasswordControllerTest {
    
    private static final String OLD_PASSWORD = "old123Passwd";
    private static final String NEW_PASSWORD = "new123Passwd";
    private static final String RE_NEW_PASSWORD = "new123Passwd";
    private static final String USER_EMAIL_NOT_FOUND = "mail123Not123FOund@gmail.com";
    private static final String USER_EMAIL = "test123test@gmail.com";

    @MockBean
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    UserService userService;

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
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName())
                .thenReturn(USER_EMAIL);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    private Optional<User> createUserDatabaseMock(String email) {
        User user = new User();
        user.setId("123id321");
        user.setName("Janusz");
        user.setSurname("Janowski");
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(OLD_PASSWORD));
        user.setRole(new Role("customer"));
        return Optional.of(user);
    }

    @Test
    public void changePasswordOkTest() throws Exception {
        String requestContent = new JSONObject()
                .put("oldPassword", OLD_PASSWORD)
                .put("newPassword", NEW_PASSWORD)
                .put("reNewPassword", RE_NEW_PASSWORD)
                .toString();
        mockMvc.perform(post("/user/changePassword")
                .contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.message").value("Password successfully changed."))
                .andDo(MockMvcRestDocumentation.document("user/changePassword/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("oldPassword").description("Old password."),
                                PayloadDocumentation.fieldWithPath("newPassword").description("New password."),
                                PayloadDocumentation.fieldWithPath("reNewPassword").description("Repeat new password.")
                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    private FieldDescriptor[] getResponseFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("code")
                        .description("Code of response status."),
                PayloadDocumentation.fieldWithPath("message")
                        .description("Message of response status."),
                };
    }
}
