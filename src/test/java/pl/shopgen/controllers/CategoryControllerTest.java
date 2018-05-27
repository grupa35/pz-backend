package pl.shopgen.controllers;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.codes.ApiStatusCode;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryDTO;
import pl.shopgen.repositories.CategoryRepository;

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
@WebMvcTest(controllers = Category.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration
@WebAppConfiguration
public class CategoryControllerTest {

    private static final String CATEGORY_ID = "test999ok999id";
    private static final String CATEGORY_NAME = "koszule";
    private static final String CATEGORY_NAME_NOT_FOUND = "bibeloty";
    private static final String CATEGORY_ID_NOT_FOUND = "test999failure999id";

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(categoryRepository.findById(Mockito.anyString()))
                .then(invocation -> {
                    if(invocation.getArgument(0).equals(CATEGORY_ID_NOT_FOUND)) {

                        return Optional.empty();
                    } else {
                        return createCategoryDatabaseMock(invocation.getArgument(0));
                    }
                });

        Mockito.when(categoryRepository.insert(Mockito.any(Category.class)))
                .then(invocation -> {
                    Category category = new Category(invocation.getArgument(0), Collections.emptyList());
                    category.setId(CATEGORY_ID);
                    return category;
                });

        Mockito.when(categoryRepository.findByName(Mockito.anyString())).then(invocation -> {
            if(invocation.getArgument(0).equals(CATEGORY_NAME_NOT_FOUND)) {
                return Optional.empty();
            } else {
                Category category = new Category(invocation.getArgument(0), Collections.emptyList());
                category.setId(CATEGORY_ID);
                return Optional.of(category);
            }
        });

        Mockito.when(categoryRepository.findAll())
                .then(invocation -> {
                    return new ArrayList<>(Collections
                            .singletonList(createCategoryDatabaseMock(CATEGORY_ID).orElse(null)));
                });

        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .then(invocation -> invocation.getArgument(0));
    }

    private Optional<Category> createCategoryDatabaseMock(String id) {
        Category category = new Category();
        category.setId(id);
        category.setName(CATEGORY_NAME);
        category.setSubcategories(Collections.emptyList());
        return Optional.of(category);
    }

    @Test
    public void getCategoryOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + CATEGORY_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.status").value(ApiStatusCode.SUCCESS))
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andDo(MockMvcRestDocumentation
                        .document("categories/getCategory/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));
    }

    @Test
    public void getCategoryNotFoundTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + CATEGORY_ID_NOT_FOUND))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").doesNotExist())
                .andExpect(jsonPath("$.name").doesNotExist())
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.status").value(ApiStatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andDo(MockMvcRestDocumentation
                        .document("categories/getCategory/not_found",
                                preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));
    }

    @Test
    public void addCategoryOkTest() throws Exception {
        String requestContent = "{\"name\": \"" + CATEGORY_NAME_NOT_FOUND + "\"}";


        mockMvc.perform(post("/categories/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME_NOT_FOUND))
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.status").value(ApiStatusCode.SUCCESS))
                .andDo(MockMvcRestDocumentation.document("categories/addCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("Name of the new category")
                        ),
                        PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));

    }

    @Test
    public void addCategoryWhenExistsTest() throws Exception {
        String requestContent = "{\"name\": \"" + CATEGORY_NAME + "\"}";

        mockMvc.perform(post("/categories/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andExpect(jsonPath("$.status").value(ApiStatusCode.OBJECT_EXISTS))
                .andDo(MockMvcRestDocumentation.document("categories/addCategory/exists",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("Name of the new category")
                        ),
                        PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));
    }

    @Test
    public void updateCategoryOkTest() throws Exception {

        String requestContent = "{" +
                getJsonParameter("id", CATEGORY_ID) + ", " +
                getJsonParameter("name", CATEGORY_NAME) +
                "}";
        mockMvc.perform(put("/categories/").contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.status").value(ApiStatusCode.SUCCESS))
                .andDo(MockMvcRestDocumentation.document("categories/updateCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id")
                                        .description("Id of existing category or empty when add"),
                                PayloadDocumentation.fieldWithPath("name")
                                        .description("New name of category with requested id")
                        ),
                        PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));
    }

    public String getJsonParameter(String parameter, String value) {
        return "\"" + parameter + "\": \"" + value + "\"";
    }

    @Test
    public void updateCategoryEmptyNameTest() throws Exception {
        String requestContent = "{" +
                getJsonParameter("id", CATEGORY_ID) + ", " +
                getJsonParameter("name", "") +
                "}";
        mockMvc.perform(put("/categories/").contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isEmpty())
                .andExpect(jsonPath("$.name").isEmpty())
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.errorMessage").isNotEmpty())
                .andExpect(jsonPath("$.status").value(ApiStatusCode.BAD_ARGUMENT))
                .andDo(MockMvcRestDocumentation.document("categories/updateCategory/emptyName",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id")
                                        .description("Id of existing category or empty when add"),
                                PayloadDocumentation.fieldWithPath("name")
                                        .description("New name of category with requested id")
                        ),
                        PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));
    }

    @Test
    public void deleteCategoryOkTest() throws Exception {
        mockMvc.perform(delete("/categories/" + CATEGORY_ID))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$.errorMessage").isEmpty())
                .andExpect(jsonPath("$.status").value(ApiStatusCode.SUCCESS))
                .andDo(MockMvcRestDocumentation.document("categories/deleteCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(CategoryDTO.fieldsDescriptor())));
    }

    @Test
    public void deleteCategorysOkTest() throws Exception {
        mockMvc.perform(delete("/categories/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[:1].subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$[:1].status").value(ApiStatusCode.SUCCESS))
                .andDo(MockMvcRestDocumentation.document("categories/deleteCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("[].status").description("Status of the response"),
                                PayloadDocumentation.fieldWithPath("[].errorMessage").type("String")
                                        .description("Description of the error."),
                                PayloadDocumentation.fieldWithPath("[].id").description("Id of the category"),
                                PayloadDocumentation.fieldWithPath("[].name").description("Name of the category")
                        )));
    }

    @Test
    public void getCategorysOkTest() throws Exception {
        mockMvc.perform(get("/categories/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[:1].subcategory").value(Collections.emptyList()))
                .andExpect(jsonPath("$[:1].status").value(ApiStatusCode.SUCCESS))
                .andDo(MockMvcRestDocumentation.document("categories/getCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("[].status").description("Status of the response"),
                                PayloadDocumentation.fieldWithPath("[].errorMessage").type("String")
                                        .description("Description of the error."),
                                PayloadDocumentation.fieldWithPath("[].id").description("Id of the category"),
                                PayloadDocumentation.fieldWithPath("[].name").description("Name of the category")
                        )));
    }
}