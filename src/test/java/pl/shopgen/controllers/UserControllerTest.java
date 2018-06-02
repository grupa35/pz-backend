package pl.shopgen.controllers;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.models.Address;
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

    private String user_id;

    private String address_id;

    private String logged_email = "test@test.pl";

    @MockBean
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        user_id = new ObjectId().toString();
        address_id = new ObjectId().toString();

        Mockito.when(userRepository.findById(Mockito.anyString()))
                .then(invocation -> Optional.of(createUserDatabaseMock(invocation.getArgument(0))));

        Mockito.when(userRepository.findAll())
                .then(invocation -> Collections.singletonList(createUserDatabaseMock(user_id)));

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenAnswer(invocation -> {
            if(invocation.getArgument(0).equals(logged_email)) {
                User user = createUserDatabaseMock(user_id);
                user.setEmail(logged_email);
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        });

        Mockito.when(authentication.getName()).thenReturn(logged_email);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
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
        user.setAddresses(Collections.singletonList(getAddressMock()));
        return user;
    }

    private Address getAddressMock() {
        Address address = new Address();
        address.setId(address_id);
        address.setAddressName("dom");
        address.setFirstName("Jan");
        address.setSecondName("Kowalski");
        address.setPostalNumber("12-123");
        address.setPostalCity("Sosnowiec");
        address.setDetails("ul. Sosnowska 15");
        return address;
    }

    @Test
    public void deleteUsersOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(user_id))
                .andExpect(jsonPath("$[:1].name").value(createUserDatabaseMock(user_id).getName()))
                .andExpect(jsonPath("$[:1].surname").value(createUserDatabaseMock(user_id).getSurname()))
                .andExpect(jsonPath("$[:1].email").value(createUserDatabaseMock(user_id).getEmail()))
                .andExpect(jsonPath("$[:1].role.id").value(createUserDatabaseMock(user_id).getRole().getId()))
                .andExpect(jsonPath("$[:1].role.name").value(createUserDatabaseMock(user_id).getRole().getName()))
                .andExpect(jsonPath("$[:1].enabled").value(createUserDatabaseMock(user_id).isEnabled()))
                .andExpect(jsonPath("$[:1].authorities").value(createUserDatabaseMock(user_id).getAuthorities()))
                .andExpect(jsonPath("$[:1].accountNonExpired")
                        .value(createUserDatabaseMock(user_id).isAccountNonExpired()))
                .andExpect(jsonPath("$[:1].accountNonLocked")
                        .value(createUserDatabaseMock(user_id).isAccountNonLocked()))
                .andExpect(jsonPath("$[:1].credentialsNonExpired")
                        .value(createUserDatabaseMock(user_id).isCredentialsNonExpired()))
                .andExpect(jsonPath("$[:1].username").value(createUserDatabaseMock(user_id).getUsername()))
                .andExpect(jsonPath("$[:1].addresses[:1].id").value(getAddressMock().getId()))
                .andExpect(jsonPath("$[:1].addresses[:1].postalCity").value(getAddressMock().getPostalCity()))
                .andExpect(jsonPath("$[:1].addresses[:1].postalNumber").value(getAddressMock().getPostalNumber()))
                .andExpect(jsonPath("$[:1].addresses[:1].firstName").value(getAddressMock().getFirstName()))
                .andExpect(jsonPath("$[:1].addresses[:1].secondName").value(getAddressMock().getSecondName()))
                .andExpect(jsonPath("$[:1].addresses[:1].details").value(getAddressMock().getDetails()))
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
                PayloadDocumentation.fieldWithPath("[].addresses").description("User addresses"),
                PayloadDocumentation.fieldWithPath("[].addresses[].id").description("Address id"),
                PayloadDocumentation.fieldWithPath("[].addresses[].addressName").description("Address name"),
                PayloadDocumentation.fieldWithPath("[].addresses[].firstName").description("Receiver first name"),
                PayloadDocumentation.fieldWithPath("[].addresses[].secondName").description("Receiver second name"),
                PayloadDocumentation.fieldWithPath("[].addresses[].postalNumber").description("Receiver postal number"),
                PayloadDocumentation.fieldWithPath("[].addresses[].postalCity").description("Receiver postal city"),
                PayloadDocumentation.fieldWithPath("[].addresses[].details").description(
                        "Receiver details like street and house number"),
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user_id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user_id))
                .andExpect(jsonPath("$.name").value(createUserDatabaseMock(user_id).getName()))
                .andExpect(jsonPath("$.surname").value(createUserDatabaseMock(user_id).getSurname()))
                .andExpect(jsonPath("$.email").value(createUserDatabaseMock(user_id).getEmail()))
                .andExpect(jsonPath("$.role.id").value(createUserDatabaseMock(user_id).getRole().getId()))
                .andExpect(jsonPath("$.role.name").value(createUserDatabaseMock(user_id).getRole().getName()))
                .andExpect(jsonPath("$.enabled").value(createUserDatabaseMock(user_id).isEnabled()))
                .andExpect(jsonPath("$.authorities").value(createUserDatabaseMock(user_id).getAuthorities()))
                .andExpect(jsonPath("$.accountNonExpired").value(createUserDatabaseMock(user_id).isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(createUserDatabaseMock(user_id).isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired")
                        .value(createUserDatabaseMock(user_id).isCredentialsNonExpired()))
                .andExpect(jsonPath("$.username").value(createUserDatabaseMock(user_id).getUsername()))
                .andExpect(jsonPath("$.addresses[:1].id").value(getAddressMock().getId()))
                .andExpect(jsonPath("$.addresses[:1].postalCity").value(getAddressMock().getPostalCity()))
                .andExpect(jsonPath("$.addresses[:1].postalNumber").value(getAddressMock().getPostalNumber()))
                .andExpect(jsonPath("$.addresses[:1].firstName").value(getAddressMock().getFirstName()))
                .andExpect(jsonPath("$.addresses[:1].secondName").value(getAddressMock().getSecondName()))
                .andExpect(jsonPath("$.addresses[:1].details").value(getAddressMock().getDetails()))
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
                PayloadDocumentation.fieldWithPath("addresses").description("User addresses"),
                PayloadDocumentation.fieldWithPath("addresses[].id").description("Address id"),
                PayloadDocumentation.fieldWithPath("addresses[].addressName").description("Address name"),
                PayloadDocumentation.fieldWithPath("addresses[].firstName").description("Receiver first name"),
                PayloadDocumentation.fieldWithPath("addresses[].secondName").description("Receiver second name"),
                PayloadDocumentation.fieldWithPath("addresses[].postalNumber").description("Receiver postal number"),
                PayloadDocumentation.fieldWithPath("addresses[].postalCity").description("Receiver postal city"),
                PayloadDocumentation.fieldWithPath("addresses[].details").description(
                        "Receiver details like street and house number"),
                PayloadDocumentation.fieldWithPath("accountNonExpired").description("Check if account is non expired"),
                PayloadDocumentation.fieldWithPath("accountNonLocked").description("Check if account is non locked"),
                PayloadDocumentation.fieldWithPath("credentialsNonExpired").description(
                        "Check if credentials are non expired"),
                PayloadDocumentation.fieldWithPath("username").type("String").description("User username. Always null"),

                };
    }

    @Test
    public void getUserOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user_id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user_id))
                .andExpect(jsonPath("$.name").value(createUserDatabaseMock(user_id).getName()))
                .andExpect(jsonPath("$.surname").value(createUserDatabaseMock(user_id).getSurname()))
                .andExpect(jsonPath("$.email").value(createUserDatabaseMock(user_id).getEmail()))
                .andExpect(jsonPath("$.role.id").value(createUserDatabaseMock(user_id).getRole().getId()))
                .andExpect(jsonPath("$.role.name").value(createUserDatabaseMock(user_id).getRole().getName()))
                .andExpect(jsonPath("$.enabled").value(createUserDatabaseMock(user_id).isEnabled()))
                .andExpect(jsonPath("$.authorities").value(createUserDatabaseMock(user_id).getAuthorities()))
                .andExpect(jsonPath("$.accountNonExpired").value(createUserDatabaseMock(user_id).isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked").value(createUserDatabaseMock(user_id).isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired")
                        .value(createUserDatabaseMock(user_id).isCredentialsNonExpired()))
                .andExpect(jsonPath("$.username").value(createUserDatabaseMock(user_id).getUsername()))
                .andExpect(jsonPath("$.addresses[:1].id").value(getAddressMock().getId()))
                .andExpect(jsonPath("$.addresses[:1].postalCity").value(getAddressMock().getPostalCity()))
                .andExpect(jsonPath("$.addresses[:1].postalNumber").value(getAddressMock().getPostalNumber()))
                .andExpect(jsonPath("$.addresses[:1].firstName").value(getAddressMock().getFirstName()))
                .andExpect(jsonPath("$.addresses[:1].secondName").value(getAddressMock().getSecondName()))
                .andExpect(jsonPath("$.addresses[:1].details").value(getAddressMock().getDetails()))
                .andDo(MockMvcRestDocumentation
                        .document("users/getUser/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void getUsersOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(user_id))
                .andExpect(jsonPath("$[:1].name").value(createUserDatabaseMock(user_id).getName()))
                .andExpect(jsonPath("$[:1].surname").value(createUserDatabaseMock(user_id).getSurname()))
                .andExpect(jsonPath("$[:1].email").value(createUserDatabaseMock(user_id).getEmail()))
                .andExpect(jsonPath("$[:1].role.id").value(createUserDatabaseMock(user_id).getRole().getId()))
                .andExpect(jsonPath("$[:1].role.name").value(createUserDatabaseMock(user_id).getRole().getName()))
                .andExpect(jsonPath("$[:1].enabled").value(createUserDatabaseMock(user_id).isEnabled()))
                .andExpect(jsonPath("$[:1].authorities").value(createUserDatabaseMock(user_id).getAuthorities()))
                .andExpect(jsonPath("$[:1].accountNonExpired")
                        .value(createUserDatabaseMock(user_id).isAccountNonExpired()))
                .andExpect(jsonPath("$[:1].accountNonLocked")
                        .value(createUserDatabaseMock(user_id).isAccountNonLocked()))
                .andExpect(jsonPath("$[:1].credentialsNonExpired")
                        .value(createUserDatabaseMock(user_id).isCredentialsNonExpired()))
                .andExpect(jsonPath("$[:1].username").value(createUserDatabaseMock(user_id).getUsername()))
                .andExpect(jsonPath("$[:1].addresses[:1].id").value(getAddressMock().getId()))
                .andExpect(jsonPath("$[:1].addresses[:1].postalCity").value(getAddressMock().getPostalCity()))
                .andExpect(jsonPath("$[:1].addresses[:1].postalNumber").value(getAddressMock().getPostalNumber()))
                .andExpect(jsonPath("$[:1].addresses[:1].firstName").value(getAddressMock().getFirstName()))
                .andExpect(jsonPath("$[:1].addresses[:1].secondName").value(getAddressMock().getSecondName()))
                .andExpect(jsonPath("$[:1].addresses[:1].details").value(getAddressMock().getDetails()))
                .andDo(MockMvcRestDocumentation
                        .document("users/getUsers/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors())));
    }

    @Test
    public void getCurrentUserOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/current"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user_id))
                .andExpect(jsonPath("$.name").value(createUserDatabaseMock(user_id).getName()))
                .andExpect(jsonPath("$.surname").value(createUserDatabaseMock(user_id).getSurname()))
                .andExpect(jsonPath("$.email").value(createUserDatabaseMock(user_id).getEmail()))
                .andExpect(jsonPath("$.role.id").value(createUserDatabaseMock(user_id).getRole().getId()))
                .andExpect(jsonPath("$.role.name").value(createUserDatabaseMock(user_id).getRole().getName()))
                .andExpect(jsonPath("$.enabled").value(createUserDatabaseMock(user_id).isEnabled()))
                .andExpect(jsonPath("$.authorities").value(createUserDatabaseMock(user_id).getAuthorities()))
                .andExpect(jsonPath("$.accountNonExpired")
                        .value(createUserDatabaseMock(user_id).isAccountNonExpired()))
                .andExpect(jsonPath("$.accountNonLocked")
                        .value(createUserDatabaseMock(user_id).isAccountNonLocked()))
                .andExpect(jsonPath("$.credentialsNonExpired")
                        .value(createUserDatabaseMock(user_id).isCredentialsNonExpired()))
                .andExpect(jsonPath("$.username").value(createUserDatabaseMock(user_id).getUsername()))
                .andExpect(jsonPath("$.addresses[:1].id").value(getAddressMock().getId()))
                .andExpect(jsonPath("$.addresses[:1].postalCity").value(getAddressMock().getPostalCity()))
                .andExpect(jsonPath("$.addresses[:1].postalNumber").value(getAddressMock().getPostalNumber()))
                .andExpect(jsonPath("$.addresses[:1].firstName").value(getAddressMock().getFirstName()))
                .andExpect(jsonPath("$.addresses[:1].secondName").value(getAddressMock().getSecondName()))
                .andExpect(jsonPath("$.addresses[:1].details").value(getAddressMock().getDetails()))
                .andDo(MockMvcRestDocumentation
                        .document("users/getCurrentUser/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }
}