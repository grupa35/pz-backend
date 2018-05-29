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
import pl.shopgen.models.Sale;
import pl.shopgen.models.SaleType;
import pl.shopgen.repositories.SaleRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import static pl.shopgen.models.SaleType.NOMINAL;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = SaleController.class, secure = false)
@AutoConfigureRestDocs(outputDir = "build/snippets")
@ContextConfiguration
@WebAppConfiguration
public class SaleControllerTest {

    private static final String SALE_ID = "sale123ok123id";
    private static final String SALE_CODE = "sale321ok321code";
    private static final String SALE_PRODUCT_ID = "sale213ok213product213id";
    private static final double SALE_PERCENT_VALUE = 0.33;
    private static final BigDecimal SALE_NOMINAL_VALUE = new BigDecimal(12.50);
    private static final LocalDate SALE_START_DATE = LocalDate.of(2018, 05, 28);
    private static final LocalDate SALE_END_DATE = LocalDate.of(2019, 06, 12);
    private static final SaleType SALE_TYPE_NOMINAL = NOMINAL;
    private static final String SALE_PRODUCT_ID_NOT_FOUND = "sale213not213ok213product213id";
    private static final String SALE_ID_NOT_FOUND = "sale123not123ok123id";
    private static final String SALE_CODE_NOT_FOUND = "sale321not321ok321code";

    @MockBean
    SaleRepository saleRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(saleRepository.findById(Mockito.anyString()))
                .then(invocation -> {
                    if(invocation.getArgument(0).equals(SALE_ID_NOT_FOUND)) {

                        return Optional.empty();
                    } else {
                        return createSaleDatabaseMock(invocation.getArgument(0));
                    }
                });

        Mockito.when(saleRepository.insert(Mockito.any(Sale.class)))
                .then(invocation -> {
                    Sale sale = new Sale(invocation.getArgument(0));
                    sale.setId(SALE_ID);
                    return sale;
                });

        Mockito.when(saleRepository.findAllByProductId(Mockito.anyString())).then(invocation -> {
            if(invocation.getArgument(0).equals(SALE_PRODUCT_ID_NOT_FOUND)) {
                return Optional.empty();
            } else {
                Sale sale = new Sale(invocation.getArgument(0));
                sale.setId(SALE_PRODUCT_ID);
                return Optional.of(sale);
            }
        });

        Mockito.when(saleRepository.findAll())
                .then(invocation -> {
                    return new ArrayList<>(Collections
                            .singletonList(createSaleDatabaseMock(SALE_ID).orElse(null)));
                });

        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).then(invocation -> invocation.getArgument(0));
    }

    private Optional<Sale> createSaleDatabaseMock(String id) {
        Sale sale = new Sale();
        sale.setId(id);
        sale.setCode(SALE_CODE);
        sale.setNominalValue(SALE_NOMINAL_VALUE);
        sale.setPercentValue(SALE_PERCENT_VALUE);
        sale.setProductId(SALE_PRODUCT_ID);
        sale.setStartDate(SALE_START_DATE);
        sale.setEndDate(SALE_END_DATE);
        sale.setActive(true);
        sale.setSaleType(SALE_TYPE_NOMINAL);
        return Optional.of(sale);
    }

    @Test
    public void getSaleOkTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/sales/" + SALE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SALE_ID))
                .andExpect(jsonPath("$.code").value(SALE_CODE))
                .andExpect(jsonPath("$.productId").value(SALE_PRODUCT_ID))
                .andExpect(jsonPath("$.percentValue").value(SALE_PERCENT_VALUE))
                .andExpect(jsonPath("$.nominalValue").value(SALE_NOMINAL_VALUE))
                .andExpect(jsonPath("$.startDate.year").value(SALE_START_DATE.getYear()))
                .andExpect(jsonPath("$.startDate.monthValue").value(SALE_START_DATE.getMonthValue()))
                .andExpect(jsonPath("$.startDate.dayOfMonth").value(SALE_START_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$.endDate.year").value(SALE_END_DATE.getYear()))
                .andExpect(jsonPath("$.endDate.monthValue").value(SALE_END_DATE.getMonthValue()))
                .andExpect(jsonPath("$.endDate.dayOfMonth").value(SALE_END_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$.saleType").value("NOMINAL"))
                .andDo(MockMvcRestDocumentation
                        .document("sales/getSale/ok", preprocessResponse(prettyPrint()),
                                PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    private FieldDescriptor[] getResponseFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("id")
                        .description("Id of the sale"),
                PayloadDocumentation.fieldWithPath("code")
                        .description("Code of the sale"),
                PayloadDocumentation.fieldWithPath("productId")
                        .description("Id of the product"),
                PayloadDocumentation.fieldWithPath("percentValue")
                        .description("Percent value of the sale"),
                PayloadDocumentation.fieldWithPath("nominalValue")
                        .description("Nominal value of the sale"),

                PayloadDocumentation.fieldWithPath("startDate.year")
                        .description("Year of start date"),
                PayloadDocumentation.fieldWithPath("startDate.month")
                        .description("Month of the start date."),
                PayloadDocumentation.fieldWithPath("startDate.monthValue")
                        .description("Month of the start date"),
                PayloadDocumentation.fieldWithPath("startDate.dayOfMonth")
                        .description("Day of the start date"),
                PayloadDocumentation.fieldWithPath("startDate.chronology.id")
                        .description("Id of chronology of start sale date."),
                PayloadDocumentation.fieldWithPath("startDate.chronology.calendarType")
                        .description("Calendar type of start date of sale."),
                PayloadDocumentation.fieldWithPath("startDate.era")
                        .description("Era of the start sale date"),
                PayloadDocumentation.fieldWithPath("startDate.dayOfYear")
                        .description("Day of year of start sale date."),
                PayloadDocumentation.fieldWithPath("startDate.dayOfWeek")
                        .description("Day of week of start sale date"),
                PayloadDocumentation.fieldWithPath("startDate.leapYear")
                        .description("Check if it's a leap year"),

                PayloadDocumentation.fieldWithPath("endDate.year")
                        .description("Year of end date"),
                PayloadDocumentation.fieldWithPath("endDate.month")
                        .description("Month of the end date."),
                PayloadDocumentation.fieldWithPath("endDate.monthValue")
                        .description("Month of end date"),
                PayloadDocumentation.fieldWithPath("endDate.dayOfMonth")
                        .description("Day of end date"),
                PayloadDocumentation.fieldWithPath("endDate.chronology.id")
                        .description("Id of chronology of end sale date."),
                PayloadDocumentation.fieldWithPath("endDate.chronology.calendarType")
                        .description("Calendar Type of end date of sale."),
                PayloadDocumentation.fieldWithPath("endDate.era")
                        .description("Era of the end sale date"),
                PayloadDocumentation.fieldWithPath("endDate.dayOfYear")
                        .description("Day of year of end sale date."),
                PayloadDocumentation.fieldWithPath("endDate.dayOfWeek")
                        .description("Day of week of end sale date"),
                PayloadDocumentation.fieldWithPath("endDate.leapYear")
                        .description("Check if it's a leap year"),

                PayloadDocumentation.fieldWithPath("saleType")
                        .description(
                        "Nominal type of sale. Percent type can be returned as well"),
                PayloadDocumentation.fieldWithPath("active").description("Is sale active")
        };

    }

    @Test
    public void addSaleOkTest() throws Exception {
        String requestContent = new JSONObject()
                .put("code", SALE_CODE_NOT_FOUND)
                .put("productId", SALE_PRODUCT_ID)
                .put("startDate", SALE_START_DATE)
                .put("endDate", SALE_END_DATE)
                .put("percentValue", SALE_PERCENT_VALUE)
                .put("nominalValue", SALE_NOMINAL_VALUE)
                .put("saleType", SALE_TYPE_NOMINAL)
                .toString();

        mockMvc.perform(post("/sales/").contentType(MediaType.APPLICATION_JSON).content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.code").value(SALE_CODE_NOT_FOUND))
                .andExpect(jsonPath("$.productId").value(SALE_PRODUCT_ID))
                .andExpect(jsonPath("$.percentValue").value(SALE_PERCENT_VALUE))
                .andExpect(jsonPath("$.nominalValue").value(SALE_NOMINAL_VALUE))
                .andExpect(jsonPath("$.startDate.year").value(SALE_START_DATE.getYear()))
                .andExpect(jsonPath("$.startDate.monthValue").value(SALE_START_DATE.getMonthValue()))
                .andExpect(jsonPath("$.startDate.dayOfMonth").value(SALE_START_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$.endDate.year").value(SALE_END_DATE.getYear()))
                .andExpect(jsonPath("$.endDate.monthValue").value(SALE_END_DATE.getMonthValue()))
                .andExpect(jsonPath("$.endDate.dayOfMonth").value(SALE_END_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$.saleType").value("NOMINAL"))
                .andDo(MockMvcRestDocumentation.document("sales/addSale/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("code").description("Code of the new sale"),
                                PayloadDocumentation.fieldWithPath("productId").description("Id of existing product."),
                                PayloadDocumentation.fieldWithPath("percentValue")
                                        .description("Percent value of the new sale"),
                                PayloadDocumentation.fieldWithPath("nominalValue")
                                        .description("Nominal value of the new sale"),
                                PayloadDocumentation.fieldWithPath("startDate")
                                        .description("Start date of the new sale"),
                                PayloadDocumentation.fieldWithPath("endDate")
                                        .description("End date of the new sale"),
                                PayloadDocumentation.fieldWithPath("saleType")
                                        .description("Nominal type of sale. Percent type can be set as well")

                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void updateSaleOkTest() throws Exception {

        String requestContent = new JSONObject()
                .put("id", SALE_ID)
                .put("code", SALE_CODE)
                .put("productId", SALE_PRODUCT_ID)
                .put("startDate", SALE_START_DATE)
                .put("endDate", SALE_END_DATE)
                .put("percentValue", SALE_PERCENT_VALUE)
                .put("nominalValue", SALE_NOMINAL_VALUE)
                .put("saleType", SALE_TYPE_NOMINAL)
                .toString();

        mockMvc.perform(put("/sales/").contentType(MediaType.APPLICATION_JSON)
                .content(requestContent))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SALE_ID))
                .andExpect(jsonPath("$.code").value(SALE_CODE))
                .andExpect(jsonPath("$.productId").value(SALE_PRODUCT_ID))
                .andExpect(jsonPath("$.percentValue").value(SALE_PERCENT_VALUE))
                .andExpect(jsonPath("$.nominalValue").value(12.50))
                .andExpect(jsonPath("$.startDate.year").value(SALE_START_DATE.getYear()))
                .andExpect(jsonPath("$.startDate.monthValue").value(SALE_START_DATE.getMonthValue()))
                .andExpect(jsonPath("$.startDate.dayOfMonth").value(SALE_START_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$.endDate.year").value(SALE_END_DATE.getYear()))
                .andExpect(jsonPath("$.endDate.monthValue").value(SALE_END_DATE.getMonthValue()))
                .andExpect(jsonPath("$.endDate.dayOfMonth").value(SALE_END_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$.saleType").value("NOMINAL"))
                .andDo(MockMvcRestDocumentation.document("sales/updateSale/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("id").description("Id of the sale."),
                                PayloadDocumentation.fieldWithPath("code").description("Code of the sale"),
                                PayloadDocumentation.fieldWithPath("productId").description("Id of existing product."),
                                PayloadDocumentation.fieldWithPath("percentValue")
                                        .description("Percent value of the new sale"),
                                PayloadDocumentation.fieldWithPath("nominalValue")
                                        .description("Nominal value of the new sale"),
                                PayloadDocumentation.fieldWithPath("startDate.")
                                        .description("Start date of the new sale."),
                                PayloadDocumentation.fieldWithPath("endDate")
                                        .description("End date of the new sale."),
                                PayloadDocumentation.fieldWithPath("saleType")
                                        .description("Nominal type of sale. Percent type can be set as well")
                        ),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void deleteSaleOkTest() throws Exception {
        mockMvc.perform(delete("/sales/" + SALE_ID))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SALE_ID))
                .andExpect(jsonPath("$.code").value(SALE_CODE))
                .andDo(MockMvcRestDocumentation.document("sales/deleteSale/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseFieldDescriptors())));
    }

    @Test
    public void deleteSalesOkTest() throws Exception {
        mockMvc.perform(delete("/sales/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(SALE_ID))
                .andExpect(jsonPath("$[:1].code").value(SALE_CODE))
                .andDo(MockMvcRestDocumentation.document("sales/deleteSale/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors()
                        )));
    }

    private FieldDescriptor[] getResponseArrayFieldDescriptors() {
        return new FieldDescriptor[]{
                PayloadDocumentation.fieldWithPath("[]id")
                        .description("Id of the sale"),
                PayloadDocumentation.fieldWithPath("[]code")
                        .description("Code of the sale"),
                PayloadDocumentation.fieldWithPath("[]productId")
                        .description("Id of the product"),
                PayloadDocumentation.fieldWithPath("[]percentValue")
                        .description("Percent value of the sale"),
                PayloadDocumentation.fieldWithPath("[]nominalValue")
                        .description("Nominal value of the sale"),

                PayloadDocumentation.fieldWithPath("[]startDate.year")
                        .description("Year of start date"),
                PayloadDocumentation.fieldWithPath("[]startDate.month")
                        .description("Month of the start date."),
                PayloadDocumentation.fieldWithPath("[]startDate.monthValue")
                        .description("Month of the start date"),
                PayloadDocumentation.fieldWithPath("[]startDate.dayOfMonth")
                        .description("Day of the start date"),
                PayloadDocumentation.fieldWithPath("[]startDate.chronology.id")
                        .description("Id of chronology of start sale date."),
                PayloadDocumentation.fieldWithPath("[]startDate.chronology.calendarType")
                        .description("Calendar type of start date of sale."),
                PayloadDocumentation.fieldWithPath("[]startDate.era")
                        .description("Era of the start sale date"),
                PayloadDocumentation.fieldWithPath("[]startDate.dayOfYear")
                        .description("Day of year of start sale date."),
                PayloadDocumentation.fieldWithPath("[]startDate.dayOfWeek")
                        .description("Day of week of start sale date"),
                PayloadDocumentation.fieldWithPath("[]startDate.leapYear")
                        .description("Check if it's a leap year"),

                PayloadDocumentation.fieldWithPath("[]endDate.year")
                        .description("Year of end date"),
                PayloadDocumentation.fieldWithPath("[]endDate.month")
                        .description("Month of the end date."),
                PayloadDocumentation.fieldWithPath("[]endDate.monthValue")
                        .description("Month of end date"),
                PayloadDocumentation.fieldWithPath("[]endDate.dayOfMonth")
                        .description("Day of end date"),
                PayloadDocumentation.fieldWithPath("[]endDate.chronology.id")
                        .description("Id of chronology of end sale date."),
                PayloadDocumentation.fieldWithPath("[]endDate.chronology.calendarType")
                        .description("Calendar Type of end date of sale."),
                PayloadDocumentation.fieldWithPath("[]endDate.era")
                        .description("Era of the end sale date"),
                PayloadDocumentation.fieldWithPath("[]endDate.dayOfYear")
                        .description("Day of year of end sale date."),
                PayloadDocumentation.fieldWithPath("[]endDate.dayOfWeek")
                        .description("Day of week of end sale date"),
                PayloadDocumentation.fieldWithPath("[]endDate.leapYear")
                        .description("Check if it's a leap year"),

                PayloadDocumentation.fieldWithPath("[]saleType")
                        .description(
                        "Nominal type of sale. Percent type can be returned as well"),
                PayloadDocumentation.fieldWithPath("[]active").description("Is sale active")
        };

    }

    @Test
    public void getSalesOkTest() throws Exception {
        mockMvc.perform(get("/sales/"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[:1].id").value(SALE_ID))
                .andExpect(jsonPath("$[:1].code").value(SALE_CODE))
                .andExpect(jsonPath("$[:1].productId").value(SALE_PRODUCT_ID))
                .andExpect(jsonPath("$[:1].percentValue").value(SALE_PERCENT_VALUE))
                .andExpect(jsonPath("$[:1].nominalValue").value(12.5))
                .andExpect(jsonPath("$[:1].startDate.year").value(SALE_START_DATE.getYear()))
                .andExpect(jsonPath("$[:1].startDate.monthValue").value(SALE_START_DATE.getMonthValue()))
                .andExpect(jsonPath("$[:1].startDate.dayOfMonth").value(SALE_START_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$[:1].endDate.year").value(SALE_END_DATE.getYear()))
                .andExpect(jsonPath("$[:1].endDate.monthValue").value(SALE_END_DATE.getMonthValue()))
                .andExpect(jsonPath("$[:1].endDate.dayOfMonth").value(SALE_END_DATE.getDayOfMonth()))
                .andExpect(jsonPath("$[:1].saleType").value("NOMINAL"))
                .andDo(MockMvcRestDocumentation.document("sales/getSale/ok",
                        preprocessResponse(prettyPrint()),
                        PayloadDocumentation.responseFields(getResponseArrayFieldDescriptors()
                        )));
    }
}