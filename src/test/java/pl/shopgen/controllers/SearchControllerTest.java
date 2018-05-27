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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.shopgen.ShopApplication;
import pl.shopgen.models.Category;
import pl.shopgen.models.Description;
import pl.shopgen.models.Product;
import pl.shopgen.repositories.CategoryRepository;
import pl.shopgen.repositories.ProductRepository;
import pl.shopgen.services.CategoryService;
import pl.shopgen.services.ICategoryService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = SearchController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration(classes = {ShopApplication.class, CategoryService.class})
@WebAppConfiguration
public class SearchControllerTest {

    private static final String CATEGORY_ID="1245";
    private static final String PRODUCT_ID="123";
    private static final String PRODUCT_NAME="asd";
    private static final String PRODUCT_URL="URL";
    private static final String PRODUCT_DESCRIPTION="product_name_description";
    private static final BigDecimal PRODUCT_PRICE=new BigDecimal(50.00).setScale(1, RoundingMode.HALF_UP);
    private static final String LOWER_PRICE="0";
    private static final String HIGHER_PRICE="100";

    @MockBean
    ProductRepository productRepository;
    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {

        Mockito.when(productRepository.findAll())
                .then(
                        invocation -> generateProducts()
                );
        Mockito.when(categoryRepository.findAll())
                .then(
                        invocation -> generateCategories()
                );

    }

    private List<Product> generateProducts() {
        List<Product> products = new ArrayList<>();
        Product p1=new Product();
        p1.setId(PRODUCT_ID);
        p1.setName(PRODUCT_NAME);
        p1.setPrice(PRODUCT_PRICE);
        p1.setImgUrl(PRODUCT_URL);
        Description d1 = new Description();
        d1.setName(PRODUCT_DESCRIPTION);
        p1.setDescription(d1);
        Category c1= new Category();
        c1.setId(CATEGORY_ID);
        p1.setCategory(c1);
        products.add(p1);
        return products;
    }

    private List<Category> generateCategories() {
        List<Category> categories = new ArrayList<>();
        Category c1 = new Category();
        c1.setId(CATEGORY_ID);
        categories.add(c1);
        return categories;
    }

    @Test
    public void searchOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/search?name="+ PRODUCT_NAME +"&categoryId="+ CATEGORY_ID + "&lowerPrice="+LOWER_PRICE+"&higherPrice="+HIGHER_PRICE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(PRODUCT_ID))
                .andExpect(jsonPath("$[:1].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[:1].price").value(50.0))
                .andExpect(jsonPath("$[:1].imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$[:1].category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].sizeToAmountMap").value(Collections.emptyMap()))
                .andExpect(jsonPath("$[:1].description.name").value(PRODUCT_DESCRIPTION))

                .andDo(MockMvcRestDocumentation
                        .document("/search/ok", preprocessResponse(prettyPrint()),
                                requestParameters(
                                        parameterWithName("name").description("Product name to search"),
                                        parameterWithName("categoryId").description("Product's categoryId to search"),
                                        parameterWithName("lowerPrice").description("The lowest price of a product"),
                                        parameterWithName("higherPrice").description("The highest price of a product")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("[].id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("[].name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("[].price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("[].imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("[].category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("[].category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("[].category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("[].sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("[].description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("[].description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("[].description.value").type("String").description("Product's description value")
                                        )));
    }

}
