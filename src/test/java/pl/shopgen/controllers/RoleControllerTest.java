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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.models.Role;
import pl.shopgen.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = RoleController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration
@WebAppConfiguration
public class RoleControllerTest {

    private static final String ROLE_ID = "id1234id1234";
    private static final String ROLE_NAME = "admin";
    private static final String ROLE_NAME_NOT_FOUND = "customer";
    private static final String ROLE_ID_NOT_FOUND = "123456789";

    @MockBean
    RoleRepository roleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(roleRepository.findById(Mockito.anyString()))
                .then(invocation -> {
                    if(invocation.getArgument(0).equals(ROLE_ID_NOT_FOUND)) {

                        return Optional.empty();
                    } else {
                        return createRoleDatabaseMock(invocation.getArgument(0));
                    }
                });

        Mockito.when(roleRepository.insert(Mockito.any(Role.class)))
                .then(invocation -> {
                    Role role = invocation.getArgument(0);
                    role.setId(ROLE_ID);
                    return role;
                });

        Mockito.when(roleRepository.findByName(Mockito.anyString())).then(invocation -> {
            if(invocation.getArgument(0).equals(ROLE_NAME_NOT_FOUND)) {
                return Optional.empty();
            } else {
                Role role = new Role(invocation.getArgument(0));
                role.setId(ROLE_ID);
                return Optional.of(role);
            }
        });

        Mockito.when(roleRepository.findAll())
                .then(invocation -> new ArrayList<>(Collections
                        .singletonList(createRoleDatabaseMock(ROLE_ID).orElse(null))));

        Mockito.when(roleRepository.save(Mockito.any(Role.class))).then(invocation -> invocation.getArgument(0));
    }

    private Optional<Role> createRoleDatabaseMock(String id) {
        Role role = new Role();
        role.setId(id);
        role.setName(ROLE_NAME);
        return Optional.of(role);
    }

    @Test
    public void getRoleOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/roles/" + ROLE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ROLE_ID))
                .andExpect(jsonPath("$.name").value(ROLE_NAME))
                .andDo(MockMvcRestDocumentation
                        .document("roles/getRole/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    private FieldDescriptor[] getResponseFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("id").description("Role id"),
                PayloadDocumentation.fieldWithPath("name").description("Role name")
        };
    }

    //    @Test
    //    public void getRoleNotFoundTest() throws Exception {
    //        mockMvc.perform(MockMvcRequestBuilders.get("/roles/" + ROLE_ID_NOT_FOUND))
    //                .andDo(print())
    //                .andExpect(status().isOk())
    //                .andExpect(jsonPath("$.id").doesNotExist())
    //                .andExpect(jsonPath("$.name").doesNotExist())
    //                .andExpect(jsonPath("$.status").value(ApiStatusCode.NOT_FOUND))
    //                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
    //                .andDo(MockMvcRestDocumentation
    //                        .document("roles/getRole/not_found",
    //                                preprocessResponse(prettyPrint()),
    //                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    //    }

    @Test
    public void addRoleOkTest() throws Exception {
        String requestContent = "{\"name\": \"" + ROLE_NAME_NOT_FOUND + "\"}";


        mockMvc.perform(post("/roles/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ROLE_ID))
                .andExpect(jsonPath("$.name").value(ROLE_NAME_NOT_FOUND))
                .andDo(MockMvcRestDocumentation.document("roles/addRole/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("New role name")
                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));

    }

    //    @Test
    //    public void addRoleWhenExistsTest() throws Exception {
    //        String requestContent = "{\"name\": \"" + ROLE_NAME + "\"}";
    //
    //        mockMvc.perform(post("/roles/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
    //                .andDo(print()).andExpect(status().isOk())
    //                .andExpect(jsonPath("$.id").value(ROLE_ID))
    //                .andExpect(jsonPath("$.name").value(ROLE_NAME))
    //                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
    //                .andExpect(jsonPath("$.status").value(ApiStatusCode.OBJECT_EXISTS))
    //                .andDo(MockMvcRestDocumentation.document("roles/addRole/exists",
    //                        preprocessResponse(prettyPrint()),
    //                        PayloadDocumentation.requestFields(
    //                                PayloadDocumentation.fieldWithPath("name").description("New role name")
    //                        ),
    //                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    //    }

    @Test
    public void updateRoleOkTest() throws Exception {
        String requestContent = new JSONObject()
                .put("id", ROLE_ID)
                .put("name", ROLE_NAME)
                .toString();

        mockMvc.perform(put("/roles/").contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ROLE_ID))
                .andExpect(jsonPath("$.name").value(ROLE_NAME))
                .andDo(MockMvcRestDocumentation.document("roles/updateRole/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id")
                                        .description("Role id. If empty role will be added"),
                                PayloadDocumentation.fieldWithPath("name")
                                        .description("Role name. When")
                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    //    @Test
    //    public void updateRoleEmptyNameTest() throws Exception {
    //        String requestContent = "{" +
    //                getJsonParameter("id", ROLE_ID) + ", " +
    //                getJsonParameter("name", "") +
    //                "}";
    //        mockMvc.perform(put("/roles/").contentType(MediaType.APPLICATION_JSON)
    //                .content(requestContent))
    //                .andDo(print()).andExpect(status().isOk())
    //                .andExpect(jsonPath("$.id").isEmpty())
    //                .andExpect(jsonPath("$.name").isEmpty())
    //                .andDo(MockMvcRestDocumentation.document("roles/updateRole/emptyName",
    //                        preprocessResponse(prettyPrint()),
    //                        PayloadDocumentation.requestFields(
    //                                PayloadDocumentation.fieldWithPath("id")
    //                                        .description("Id of existing role or empty when add"),
    //                                PayloadDocumentation.fieldWithPath("name")
    //                                        .description("New name of role with requested id")
    //                        ),
    //                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    //    }

    @Test
    public void deleteRoleOkTest() throws Exception {
        mockMvc.perform(delete("/roles/" + ROLE_ID))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ROLE_ID))
                .andExpect(jsonPath("$.name").value(ROLE_NAME))
                .andDo(MockMvcRestDocumentation.document("roles/deleteRole/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void deleteRolesOkTest() throws Exception {
        mockMvc.perform(delete("/roles/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(ROLE_ID))
                .andExpect(jsonPath("$[:1].name").value(ROLE_NAME))
                .andDo(MockMvcRestDocumentation.document("roles/deleteRoles/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getArrayResponseFieldDescriptors())));
    }

    private FieldDescriptor[] getArrayResponseFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("[].id").description("Role id"),
                PayloadDocumentation.fieldWithPath("[].name").description("Role name")
        };
    }

    @Test
    public void getRolesOkTest() throws Exception {
        mockMvc.perform(get("/roles/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(ROLE_ID))
                .andExpect(jsonPath("$[:1].name").value(ROLE_NAME))
                .andDo(MockMvcRestDocumentation.document("roles/getRoles/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getArrayResponseFieldDescriptors())));
    }

}