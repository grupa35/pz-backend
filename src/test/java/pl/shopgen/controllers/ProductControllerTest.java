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
import pl.shopgen.models.*;
import pl.shopgen.repositories.ProductRepository;
import pl.shopgen.services.IProductSaleService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration
@WebAppConfiguration
public class ProductControllerTest {
    private final static String PRODUCT_ID = "123";
    private static final String PRODUCT_NAME="PRODUCT NAME";
    private static final String PRODUCT_URL="URL";
    private static final String DESCRIPTION_VALUE="DESCRIPTION VALUE";
    private static final String DESCRIPTION_ID="124";
    private static final String DESCRIPTION_NAME="DESCRIPTION NAME";
    private static final String CATEGORY_ID = "125";
    private static final String CATEGORY_NAME = "CATEGORY NAME";
    private static final BigDecimal PRODUCT_PRICE=new BigDecimal(50.00).setScale(1, RoundingMode.HALF_UP);

    private static final Boolean IS_SALE = true;
    private static final String SALE_CODE = "SALE CODE";
    private static final SaleType SALE_TYPE = SaleType.PERCENT;
    private static final BigDecimal SALE_NOMINAL_VALUE = new BigDecimal(50.00).setScale(1, RoundingMode.HALF_UP);
    private static final double SALE_PERCENT_VALUE = 0.75;
    private static final BigDecimal PRICE_AFTER_SALE = new BigDecimal(50.00).setScale(1, RoundingMode.HALF_UP);
    private static final BigDecimal SALE_VALUE = new BigDecimal(50.00).setScale(1, RoundingMode.HALF_UP);


    @MockBean
    ProductRepository productRepository;
    @MockBean
    IProductSaleService productSaleService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp(){
        Mockito.when(productRepository.findById(PRODUCT_ID))
                .then(invocation -> generateProduct());

        Mockito.when(productRepository.findAll())
                .then(invocation -> {
                    List<Product> products = new ArrayList<>();
                    products.add(generateProduct().get());
                    return products;
                });
        Mockito.when(productRepository.insert(Mockito.any(Product.class)))
                .then(invocation -> {
                    Product product = invocation.getArgument(0);
                    product.setId(PRODUCT_ID);
                    return product;
                });
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .then(
                        invocation -> invocation.getArgument(0)
                );
        Mockito.when(productSaleService.getSaleDTO(generateProduct().get()))
                .then(invocation -> {
                    SaleDTO sale = SaleDTO.builder()
                            .isSale(IS_SALE)
                            .code(SALE_CODE)
                            .saleType(SALE_TYPE)
                            .saleNominalValue(SALE_NOMINAL_VALUE)
                            .salePercentValue(SALE_PERCENT_VALUE)
                            .priceAfterSale(PRICE_AFTER_SALE)
                            .saleValue(SALE_VALUE)
                            .build();
                    return sale;
                });
    }

    private Optional<Product> generateProduct() {
        Product p = new Product();
        Description d = new Description();
        Category c = new Category();

        p.setId(PRODUCT_ID);
        p.setImgUrl(PRODUCT_URL);
        p.setName(PRODUCT_NAME);
        p.setPrice(PRODUCT_PRICE);
        p.setSizeToAmountMap(Collections.emptyMap());

        c.setId(CATEGORY_ID);
        c.setName(CATEGORY_NAME);
        c.setSubcategories(Collections.emptyList());

        d.setId(DESCRIPTION_ID);
        d.setName(DESCRIPTION_NAME);
        d.setValue(DESCRIPTION_VALUE);

        p.setCategory(c);
        p.setDescription(d);

        return Optional.of(p);
    }

    @Test
    public void getProductOkTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + PRODUCT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.price").value(50.0))
                .andExpect(jsonPath("$.imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$.sizeToAmountMap").isEmpty())
                .andExpect(jsonPath("$.category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.category.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.category.subcategories").isEmpty())
                .andExpect(jsonPath("$.description.name").value(DESCRIPTION_NAME))
                .andExpect(jsonPath("$.description.value").value(DESCRIPTION_VALUE))
                .andExpect(jsonPath("$.description.id").value(DESCRIPTION_ID))
                .andDo(MockMvcRestDocumentation
                        .document("products/getProduct/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("description.value").type("String").description("Product's description value")
                                )));
    }

    @Test
    public void getProductsOkTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(PRODUCT_ID))
                .andExpect(jsonPath("$[:1].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[:1].price").value(50.0))
                .andExpect(jsonPath("$[:1].imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$[:1].sizeToAmountMap").value(Collections.emptyMap()))
                .andExpect(jsonPath("$[:1].category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].category.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[:1].category.subcategories").isNotEmpty())
                .andExpect(jsonPath("$[:1].description.name").value(DESCRIPTION_NAME))
                .andExpect(jsonPath("$[:1].description.value").value(DESCRIPTION_VALUE))
                .andExpect(jsonPath("$[:1].description.id").value(DESCRIPTION_ID))
                .andDo(MockMvcRestDocumentation
                        .document("products/getProducts/ok", preprocessResponse(prettyPrint()),
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

    @Test
    public void addProductOkTest() throws Exception{

        String requestContent = "" +
                "{\"id\":\"" + PRODUCT_ID + "\"," +
                "\"name\":\"" + PRODUCT_NAME + "\"," +
                "\"price\":" + PRODUCT_PRICE + "," +
                "\"imgUrl\":\"" + PRODUCT_URL + "\"," +
                "\"category\":{" +
                    "\"id\":\"" + CATEGORY_ID + "\"," +
                    "\"name\":\"" + CATEGORY_NAME + "\"," +
                 "\"subcategories\":[]}," +
                "\"sizeToAmountMap\":{}," +
                "\"description\":{\"" +
                    "id\":\"" + DESCRIPTION_ID + "\"," +
                    "\"name\":\"" + DESCRIPTION_NAME + "\"," +
                    "\"value\":\"" + DESCRIPTION_VALUE + "\"}}";

        mockMvc.perform(MockMvcRequestBuilders.post("/products/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.price").value(50.0))
                .andExpect(jsonPath("$.imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$.sizeToAmountMap").isEmpty())
                .andExpect(jsonPath("$.category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.category.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.category.subcategories").isEmpty())
                .andExpect(jsonPath("$.description.name").value(DESCRIPTION_NAME))
                .andExpect(jsonPath("$.description.value").value(DESCRIPTION_VALUE))
                .andExpect(jsonPath("$.description.id").value(DESCRIPTION_ID))
                .andDo(MockMvcRestDocumentation
                        .document("products/addProduct/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.requestFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("description.value").type("String").description("Product's description value")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("description.value").type("String").description("Product's description value")
                                )));
    }

    @Test
    public void updateProductOkTest() throws Exception{

        String requestContent = "" +
                "{\"id\":\"" + PRODUCT_ID + "\"," +
                "\"name\":\"" + PRODUCT_NAME + "\"," +
                "\"price\":" + PRODUCT_PRICE + "," +
                "\"imgUrl\":\"" + PRODUCT_URL + "\"," +
                "\"category\":{" +
                "\"id\":\"" + CATEGORY_ID + "\"," +
                "\"name\":\"" + CATEGORY_NAME + "\"," +
                "\"subcategories\":[]}," +
                "\"sizeToAmountMap\":{}," +
                "\"description\":{\"" +
                "id\":\"" + DESCRIPTION_ID + "\"," +
                "\"name\":\"" + DESCRIPTION_NAME + "\"," +
                "\"value\":\"" + DESCRIPTION_VALUE + "\"}}";

        mockMvc.perform(MockMvcRequestBuilders.put("/products/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.price").value(50.0))
                .andExpect(jsonPath("$.imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$.sizeToAmountMap").isEmpty())
                .andExpect(jsonPath("$.category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.category.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.category.subcategories").isEmpty())
                .andExpect(jsonPath("$.description.name").value(DESCRIPTION_NAME))
                .andExpect(jsonPath("$.description.value").value(DESCRIPTION_VALUE))
                .andExpect(jsonPath("$.description.id").value(DESCRIPTION_ID))
                .andDo(MockMvcRestDocumentation
                        .document("products/updateProduct/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.requestFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("description.value").type("String").description("Product's description value")
                                ),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("description.value").type("String").description("Product's description value")
                                )));
    }

    @Test
    public void deleteProductOkTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/" + PRODUCT_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID))
                .andExpect(jsonPath("$.name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$.price").value(50.0))
                .andExpect(jsonPath("$.imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$.sizeToAmountMap").isEmpty())
                .andExpect(jsonPath("$.category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$.category.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$.category.subcategories").isEmpty())
                .andExpect(jsonPath("$.description.name").value(DESCRIPTION_NAME))
                .andExpect(jsonPath("$.description.value").value(DESCRIPTION_VALUE))
                .andExpect(jsonPath("$.description.id").value(DESCRIPTION_ID))
                .andDo(MockMvcRestDocumentation
                        .document("products/deleteProduct/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("id").description("Id of the product"),
                                        PayloadDocumentation.fieldWithPath("name").type("String").description("Name of the product"),
                                        PayloadDocumentation.fieldWithPath("price").type("BigDecimal").description("Price of the product"),
                                        PayloadDocumentation.fieldWithPath("imgUrl").description("Url of product's image"),
                                        PayloadDocumentation.fieldWithPath("category.id").description("Product's category id"),
                                        PayloadDocumentation.fieldWithPath("category.name").type("String").description("Product's category name"),
                                        PayloadDocumentation.fieldWithPath("category.subcategories").description("Product's category subcategories"),
                                        PayloadDocumentation.fieldWithPath("sizeToAmountMap").type("Map").description("Amount of concrete product's size"),
                                        PayloadDocumentation.fieldWithPath("description.id").type("String").description("Product's description id"),
                                        PayloadDocumentation.fieldWithPath("description.name").type("String").description("Product's description name"),
                                        PayloadDocumentation.fieldWithPath("description.value").type("String").description("Product's description value")
                                )));

    }

    @Test
    public void deleteProductsOkTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(PRODUCT_ID))
                .andExpect(jsonPath("$[:1].name").value(PRODUCT_NAME))
                .andExpect(jsonPath("$[:1].price").value(50.0))
                .andExpect(jsonPath("$[:1].imgUrl").value(PRODUCT_URL))
                .andExpect(jsonPath("$[:1].sizeToAmountMap").isNotEmpty())
                .andExpect(jsonPath("$[:1].category.id").value(CATEGORY_ID))
                .andExpect(jsonPath("$[:1].category.name").value(CATEGORY_NAME))
                .andExpect(jsonPath("$[:1].category.subcategories").isNotEmpty())
                .andExpect(jsonPath("$[:1].description.name").value(DESCRIPTION_NAME))
                .andExpect(jsonPath("$[:1].description.value").value(DESCRIPTION_VALUE))
                .andExpect(jsonPath("$[:1].description.id").value(DESCRIPTION_ID))
                .andDo(MockMvcRestDocumentation
                        .document("products/deleteProducts/ok", preprocessResponse(prettyPrint()),
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

    @Test
    public void getProductSaleOkTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/" + PRODUCT_ID + "/sale"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSale").value(IS_SALE))
                .andExpect(jsonPath("$.code").value(SALE_CODE))
                .andExpect(jsonPath("$.saleType").value(String.valueOf(SALE_TYPE)))
                .andExpect(jsonPath("$.saleNominalValue").value(SALE_NOMINAL_VALUE.doubleValue()))
                .andExpect(jsonPath("$.salePercentValue").value(SALE_PERCENT_VALUE))
                .andExpect(jsonPath("$.priceAfterSale").value(PRICE_AFTER_SALE.doubleValue()))
                .andExpect(jsonPath("$.saleValue").value(SALE_VALUE.doubleValue()))
                .andDo(MockMvcRestDocumentation
                        .document("products/getProductSale/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(
                                        PayloadDocumentation.fieldWithPath("isSale").type("Boolean").description("Is sale current"),
                                        PayloadDocumentation.fieldWithPath("code").type("String").description("Code of sale"),
                                        PayloadDocumentation.fieldWithPath("saleType").type("String").description("Type of sale"),
                                        PayloadDocumentation.fieldWithPath("saleNominalValue").type("Big Decimal").description("Nominal value of sale"),
                                        PayloadDocumentation.fieldWithPath("salePercentValue").type("Double").description("Percent value of sale"),
                                        PayloadDocumentation.fieldWithPath("priceAfterSale").type("Big Decimal").description("Price after sale"),
                                        PayloadDocumentation.fieldWithPath("saleValue").type("Big Decimal").description("Value of sale")
                                )));

    }
}
