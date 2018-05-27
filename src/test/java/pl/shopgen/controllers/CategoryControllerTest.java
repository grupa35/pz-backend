package pl.shopgen.controllers;

import org.json.JSONArray;
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
import pl.shopgen.ShopApplication;
import pl.shopgen.models.Category;
import pl.shopgen.repositories.CategoryRepository;
import pl.shopgen.services.CategoryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = CategoryController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration(classes = {CategoryService.class, ShopApplication.class})
@WebAppConfiguration
public class CategoryControllerTest {

    private static final String CATEGORY_ID = "test999ok999id";
    private static final String CATEGORY_NAME = "koszule";
    private static final String CATEGORY_NAME_NOT_FOUND = "bibeloty";
    private static final String CATEGORY_ID_NOT_FOUND = "test999failure999id";

    @MockBean
    private CategoryRepository categoryRepository;

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
                .then(invocation -> new ArrayList<>(Collections
                        .singletonList(createCategoryDatabaseMock(CATEGORY_ID).orElse(null))));

        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .then(invocation -> invocation.getArgument(0));
    }

    private Optional<Category> createCategoryDatabaseMock(String id) {
        Category category = new Category();
        category.setId(id);
        category.setName(CATEGORY_NAME);
        category.setSubcategories(new ArrayList<>());
        return Optional.of(category);
    }

    @Test
    public void getCategoryOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + CATEGORY_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategories").isArray())
                .andDo(MockMvcRestDocumentation
                        .document("categories/getCategory/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    //    @Test
    //    public void getCategoryNotFoundTest() throws Exception {
    //        mockMvc.perform(MockMvcRequestBuilders.get("/categories/" + CATEGORY_ID_NOT_FOUND))
    //                .andDo(print())
    //                .andExpect(status().isOk())
    //                .andExpect(jsonPath("$.id").doesNotExist())
    //                .andExpect(jsonPath("$.name").doesNotExist())
    //                .andExpect(jsonPath("$.subcategories").value(Collections.emptyList()))
    //                .andDo(MockMvcRestDocumentation
    //                        .document("categories/getCategory/not_found",
    //                                preprocessResponse(prettyPrint()),
    //                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    //    }

    private FieldDescriptor[] getResponseFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("id").description("Id of the category"),
                PayloadDocumentation.fieldWithPath("name").description("Name of the category"),
                PayloadDocumentation.fieldWithPath("subcategories").description("List of subcategories")
        };
    }

    //    @Test
    //    public void addCategoryWhenExistsTest() throws Exception {
    //        String requestContent = "{\"name\": \"" + CATEGORY_NAME + "\"}";
    //
    //        mockMvc.perform(post("/categories/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
    //                .andDo(print()).andExpect(status().isOk())
    //                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
    //                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
    //                .andExpect(jsonPath("$.subcategories").value(Collections.emptyList()))
    //                .andDo(MockMvcRestDocumentation.document("categories/addNewCategory/exists",
    //                        preprocessResponse(prettyPrint()),
    //                        PayloadDocumentation.requestFields(
    //                                PayloadDocumentation.fieldWithPath("name").description("Name of the new category")
    //                        ),
    //                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    //    }

    @Test
    public void addCategoryOkTest() throws Exception {
        String requestContent = new JSONObject()
                .put("name", CATEGORY_NAME_NOT_FOUND)
                .put("parentCategoryId", CATEGORY_ID)
                .toString();

        mockMvc.perform(post("/categories").contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME_NOT_FOUND))
                .andExpect(jsonPath("$.subcategories").isArray())
                .andDo(MockMvcRestDocumentation.document("categories/addNewCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("name").description("Name of the new category"),
                                PayloadDocumentation.fieldWithPath("parentCategoryId")
                                        .description("Parent category id under which new category will be added")
                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));

    }

    @Test
    public void updateCategoryOkTest() throws Exception {
        String requestContent = new JSONObject()
                .put("id", CATEGORY_ID)
                .put("name", CATEGORY_NAME)
                .put("subcategories", new JSONArray())
                .toString();

        mockMvc.perform(put("/categories/").contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategories").isArray())
                .andDo(MockMvcRestDocumentation.document("categories/updateCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id")
                                        .description("Id of existing category or empty when add"),
                                PayloadDocumentation.fieldWithPath("name")
                                        .description("New name of category with requested id"),
                                PayloadDocumentation.fieldWithPath("subcategories[]")
                                        .description("Subcategories")
                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    //    @Test
    //    public void updateCategoryEmptyNameTest() throws Exception {
    //        String requestContent = "{" +
    //                getJsonParameter("id", CATEGORY_ID) + ", " +
    //                getJsonParameter("name", "") +
    //                "}";
    //        mockMvc.perform(put("/categories/").contentType(MediaType.APPLICATION_JSON)
    //                .content(requestContent))
    //                .andDo(print()).andExpect(status().isOk())
    //                .andExpect(jsonPath("$.id").isEmpty())
    //                .andExpect(jsonPath("$.name").isEmpty())
    //                .andExpect(jsonPath("$.subcategories").value(Collections.emptyList()))
    //                .andDo(MockMvcRestDocumentation.document("categories/updateCategory/emptyName",
    //                        preprocessResponse(prettyPrint()),
    //                        PayloadDocumentation.requestFields(
    //                                PayloadDocumentation.fieldWithPath("id")
    //                                        .description("Id of existing category or empty when add"),
    //                                PayloadDocumentation.fieldWithPath("name")
    //                                        .description("New name of category with requested id")
    //                        ),
    //                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    //    }

    @Test
    public void deleteCategoryOkTest() throws Exception {
        mockMvc.perform(delete("/categories/" + CATEGORY_ID))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.subcategories").isArray())
                .andDo(MockMvcRestDocumentation.document("categories/deleteCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void deleteCategoriesOkTest() throws Exception {
        mockMvc.perform(delete("/categories/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[:1].subcategories").isArray())
                .andDo(MockMvcRestDocumentation.document("categories/deleteCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors())));
    }

    private FieldDescriptor[] getResponseArrayFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("[].id").description("Id of the category"),
                PayloadDocumentation.fieldWithPath("[].name").description("Name of the category"),
                PayloadDocumentation.fieldWithPath("[].subcategories").description("List of subcategories")
        };
    }

    @Test
    public void getCategoriesOkTest() throws Exception {
        mockMvc.perform(get("/categories/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[:1].subcategories").isArray())
                .andDo(MockMvcRestDocumentation.document("categories/getCategory/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors())));
    }
}