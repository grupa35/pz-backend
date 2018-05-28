package pl.shopgen.controllers;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.models.Role;
import pl.shopgen.models.User;
import pl.shopgen.repositories.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = UserController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration
@WebAppConfiguration
public class UserControllerTest {

    private String USER_ID;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        USER_ID = new ObjectId().toString();

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .then(invocation -> Optional.of(createUserDatabaseMock(invocation.getArgument(0))));

        Mockito.when(userRepository.findAll())
                .then(invocation -> Collections.singletonList(createUserDatabaseMock(USER_ID)));
    }

    private User createUserDatabaseMock(String userId) {
        User user = new User();
        user.setId(userId);
        user.setEmail("test@test.pl");
        user.setPassword("password");
        user.setEnabled(true);
        user.setSurname("surname");
        user.setName("name");
        Role role = new Role("customer");
        role.setId("987655");
        user.setRole(role);
        return user;
    }

    @Test
    public void deleteUsersOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(USER_ID))
                .andExpect(jsonPath("$[:1].name").value(createUserDatabaseMock(USER_ID).getName()))
                .andExpect(jsonPath("$[:1].surname").value(createUserDatabaseMock(USER_ID).getSurname()))
                .andExpect(jsonPath("$[:1].email").value(createUserDatabaseMock(USER_ID).getEmail()))
                .andExpect(jsonPath("$[:1].role.id").value(createUserDatabaseMock(USER_ID).getRole().getId()))
                .andExpect(jsonPath("$[:1].role.name").value(createUserDatabaseMock(USER_ID).getRole().getName()))
                .andExpect(jsonPath("$[:1].enabled").value(createUserDatabaseMock(USER_ID).isEnabled()))
                .andExpect(jsonPath("$[:1].authorities").value(createUserDatabaseMock(USER_ID).getAuthorities()))
                .andExpect(jsonPath("$[:1].accountNonExpired")
                        .value(createUserDatabaseMock(USER_ID).isAccountNonExpired()))
                .andExpect(jsonPath("$[:1].accountNonLocked")
                        .value(createUserDatabaseMock(USER_ID).isAccountNonLocked()))
                .andExpect(jsonPath("$[:1].credentialsNonExpired")
                        .value(createUserDatabaseMock(USER_ID).isCredentialsNonExpired()))
                .andExpect(jsonPath("$[:1].username").value(createUserDatabaseMock(USER_ID).getUsername()))
                .andDo(MockMvcRestDocumentation
                        .document("users/deleteUsers/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors())));
    }

    private FieldDescriptor[] getResponseArrayFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("[].id").description("User id"),
                PayloadDocumentation.fieldWithPath("[].name").description("User name"),
                PayloadDocumentation.fieldWithPath("[].surname").description("User surname"),
                PayloadDocumentation.fieldWithPath("[].email").description("User email. Use it to log in"),
                PayloadDocumentation.fieldWithPath("[].role").description("Role belonging to user"),
                PayloadDocumentation.fieldWithPath("[].role.id").description("Role id"),
                PayloadDocumentation.fieldWithPath("[].role.name").description("Role name"),
                PayloadDocumentation.fieldWithPath("[].enabled").description("Check if user is enabled"),
                PayloadDocumentation.fieldWithPath("[].authorities").description("..."),
                PayloadDocumentation
                        .fieldWithPath("[].accountNonExpired").description("Check if account is non expired"),
                PayloadDocumentation.fieldWithPath("[].accountNonLocked").description("Check if account is non locked"),
                PayloadDocumentation.fieldWithPath("[].credentialsNonExpired").description(
                        "Check if credentials are non expired"),
                PayloadDocumentation.fieldWithPath("[].username").type("String").description(
                        "User username. Always null"),
                };
    }

    @Test
    public void deleteUserOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(createUserDatabaseMock(USER_ID).getName()))
                .andExpect(jsonPath("$.surname").value(createUserDatabaseMock(USER_ID).getSurname()))
                .andExpect(jsonPath("$.email").value(createUserDatabaseMock(USER_ID).getEmail()))
                .andExpect(jsonPath("$.role.id").value(createUserDatabaseMock(USER_ID).getRole().getId()))
                .andExpect(jsonPath("$.role.name").value(createUserDatabaseMock(USER_ID).getRole().getName()))
                .andExpect(jsonPath("$.enabled").value(createUserDatabaseMock(USER_ID).isEnabled()))
                .andExpect(jsonPath("$.authorities").value(createUserDatabaseMock(USER_ID).getAuthorities()))
                .andExpect(jsonPath("$.accountNonExpired").value(createUserDatabaseMock(USER_ID).isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(createUserDatabaseMock(USER_ID).isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired")
                        .value(createUserDatabaseMock(USER_ID).isCredentialsNonExpired()))
                .andExpect(jsonPath("$.username").value(createUserDatabaseMock(USER_ID).getUsername()))
                .andDo(MockMvcRestDocumentation
                        .document("users/deleteUser/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    private FieldDescriptor[] getResponseFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("id").description("User id"),
                PayloadDocumentation.fieldWithPath("name").description("User name"),
                PayloadDocumentation.fieldWithPath("surname").description("User surname"),
                PayloadDocumentation.fieldWithPath("email").description("User email. Use it to log in"),
                PayloadDocumentation.fieldWithPath("role").description("Role belonging to user"),
                PayloadDocumentation.fieldWithPath("role.id").description("Role id"),
                PayloadDocumentation.fieldWithPath("role.name").description("Role name"),
                PayloadDocumentation.fieldWithPath("enabled").description("Check if user is enabled"),
                PayloadDocumentation.fieldWithPath("authorities").description("..."),
                PayloadDocumentation.fieldWithPath("accountNonExpired").description("Check if account is non expired"),
                PayloadDocumentation.fieldWithPath("accountNonLocked").description("Check if account is non locked"),
                PayloadDocumentation.fieldWithPath("credentialsNonExpired").description(
                        "Check if credentials are non expired"),
                PayloadDocumentation.fieldWithPath("username").type("String").description("User username. Always null"),

                };
    }

    @Test
    public void getUserOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(USER_ID))
                .andExpect(jsonPath("$.name").value(createUserDatabaseMock(USER_ID).getName()))
                .andExpect(jsonPath("$.surname").value(createUserDatabaseMock(USER_ID).getSurname()))
                .andExpect(jsonPath("$.email").value(createUserDatabaseMock(USER_ID).getEmail()))
                .andExpect(jsonPath("$.role.id").value(createUserDatabaseMock(USER_ID).getRole().getId()))
                .andExpect(jsonPath("$.role.name").value(createUserDatabaseMock(USER_ID).getRole().getName()))
                .andExpect(jsonPath("$.enabled").value(createUserDatabaseMock(USER_ID).isEnabled()))
                .andExpect(jsonPath("$.authorities").value(createUserDatabaseMock(USER_ID).getAuthorities()))
                .andExpect(jsonPath("$.accountNonExpired").value(createUserDatabaseMock(USER_ID).isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(createUserDatabaseMock(USER_ID).isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired")
                        .value(createUserDatabaseMock(USER_ID).isCredentialsNonExpired()))
                .andExpect(jsonPath("$.username").value(createUserDatabaseMock(USER_ID).getUsername()))
                .andDo(MockMvcRestDocumentation
                        .document("users/getUser/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void getUsersOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(USER_ID))
                .andExpect(jsonPath("$[:1].name").value(createUserDatabaseMock(USER_ID).getName()))
                .andExpect(jsonPath("$[:1].surname").value(createUserDatabaseMock(USER_ID).getSurname()))
                .andExpect(jsonPath("$[:1].email").value(createUserDatabaseMock(USER_ID).getEmail()))
                .andExpect(jsonPath("$[:1].role.id").value(createUserDatabaseMock(USER_ID).getRole().getId()))
                .andExpect(jsonPath("$[:1].role.name").value(createUserDatabaseMock(USER_ID).getRole().getName()))
                .andExpect(jsonPath("$[:1].enabled").value(createUserDatabaseMock(USER_ID).isEnabled()))
                .andExpect(jsonPath("$[:1].authorities").value(createUserDatabaseMock(USER_ID).getAuthorities()))
                .andExpect(jsonPath("$[:1].accountNonExpired")
                        .value(createUserDatabaseMock(USER_ID).isAccountNonExpired()))
                .andExpect(jsonPath("$[:1].accountNonLocked")
                        .value(createUserDatabaseMock(USER_ID).isAccountNonLocked()))
                .andExpect(jsonPath("$[:1].credentialsNonExpired")
                        .value(createUserDatabaseMock(USER_ID).isCredentialsNonExpired()))
                .andExpect(jsonPath("$[:1].username").value(createUserDatabaseMock(USER_ID).getUsername()))
                .andDo(MockMvcRestDocumentation
                        .document("users/getUsers/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors())));
    }
}