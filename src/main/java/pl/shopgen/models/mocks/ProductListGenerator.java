package pl.shopgen.models.mocks;

import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;
import pl.shopgen.models.Description;
import pl.shopgen.models.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductListGenerator {

    private CategoryRepository categoryRepository;

    private List<Product> products = new ArrayList<>();

    private Description description = new Description();

    private List<Category> categories = new ArrayList<>();

    private Product p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15;


    public ProductListGenerator(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        categories.addAll(categoryRepository.findAll());
    }

    public List<Product> generateProducts() {
        products.clear();
        categories.clear();
        categories.addAll(categoryRepository.findAll());
        /* Produkt 1 */
        //        kobieta -> koszule sukienki
        p1 = new Product();
        description.setName("Opis");
        description.setValue("Sukienka z dekoltem w serek.");

        p1.setCategory(getCategoryByName("kobieta", "sukienki", "wieczorowe"));
        p1.setImgUrl("/resources/img/sukienka_z_dekoltem_w_serek0151b375-8c94-49d9-a60b-797232dfe62a.jpg");
        p1.setName("New Look");
        p1.setPrice(new BigDecimal(79.90));
        p1.getSizeToAmountMap().put("32", 25);
        p1.getSizeToAmountMap().put("34", 34);
        p1.getSizeToAmountMap().put("36", 44);
        p1.getSizeToAmountMap().put("38", 40);
        p1.getSizeToAmountMap().put("40", 20);
        p1.getSizeToAmountMap().put("42", 20);
        p1.getSizeToAmountMap().put("44", 20);
        p1.getSizeToAmountMap().put("46", 25);
        p1.getSizeToAmountMap().put("48", 23);
        p1.getSizeToAmountMap().put("50", 15);
        p1.setDescription(description);

        /* Produkt 2 */
        p2 = new Product();
        description.setName("Opis");
        description.setValue("Dłuższa marynarka z elastycznej takniny.");
        p2.setCategory(getCategoryByName("kobieta"));
        p2.setImgUrl("/resources/img/dluga_marynarka404e9249-978e-403f-aa5d-c3abdba71553.jpg");
        p2.setName("Topshop");
        p2.setPrice(new BigDecimal(149.90));
        p2.getSizeToAmountMap().put("32", 15);
        p2.getSizeToAmountMap().put("34", 18);
        p2.getSizeToAmountMap().put("36", 21);
        p2.getSizeToAmountMap().put("38", 33);
        p2.getSizeToAmountMap().put("40", 40);
        p2.getSizeToAmountMap().put("42", 22);
        p2.getSizeToAmountMap().put("44", 25);
        p2.getSizeToAmountMap().put("46", 27);
        p2.getSizeToAmountMap().put("48", 12);
        p2.getSizeToAmountMap().put("50", 10);
        p2.setDescription(description);

        /* Produkt 3 */
        p3 = new Product();
        description.setName("Opis");
        description.setValue("Szorty z bawełnianej popeliny ze sznurkiem.");
        p3.setCategory(getCategoryByName("dziecko", "chlopiec", "1 - 4 lata"));
        p3.setImgUrl("/resources/img/bawelniane_szortyfa002ebb-1325-4ccc-aa4f-7d0568fc14a2.jpg");
        p3.setName("Benetton");
        p3.setPrice(new BigDecimal(29.90));
        p3.getSizeToAmountMap().put("92", 26);
        p3.getSizeToAmountMap().put("98", 30);
        p3.getSizeToAmountMap().put("104", 40);
        p3.getSizeToAmountMap().put("110", 20);
        p3.getSizeToAmountMap().put("116", 28);
        p3.getSizeToAmountMap().put("122", 18);
        p3.getSizeToAmountMap().put("128", 30);
        p3.getSizeToAmountMap().put("134", 33);
        p3.getSizeToAmountMap().put("140", 25);
        p3.setDescription(description);

        /* Produkt 4 */
        p4 = new Product();
        description.setName("Opis");
        description.setValue("Sznurowane buty z bawełny.");
        p4.setCategory(getCategoryByName("mezczyzna"));
        p4.setImgUrl("/resources/img/buty_sportowef74240d1-4103-4d46-b61a-4cf94d4dc05e.jpg");
        p4.setName("Adidas");
        p4.setPrice(new BigDecimal(299.00));
        p4.getSizeToAmountMap().put("42", 15);
        p4.getSizeToAmountMap().put("46", 15);
        p4.setDescription(description);


        /* Produkt 5 */
        p5 = new Product();
        description.setName("Opis");
        description.setValue("Bawełniana koszulka z kieszenią.");
        p5.setCategory(getCategoryByName("mezczyzna", "koszule/koszulki", "koszule"));
        p5.setImgUrl("/resources/img/bawelniana_koszulkac5eb3003-e267-4e0d-99b0-99c7c2583c3b.jpg");
        p5.setName("Nike");
        p5.setPrice(new BigDecimal(79.99));
        p5.getSizeToAmountMap().put("S", 20);
        p5.getSizeToAmountMap().put("M", 30);
        p5.getSizeToAmountMap().put("L", 40);
        p5.getSizeToAmountMap().put("XL", 30);
        p5.setDescription(description);

        /* Produkt 6 */
        p6 = new Product();
        description.setName("Opis");
        description.setValue("Jeansy z postrzępionymi nogawkami.");
        p6.setCategory(getCategoryByName("kobieta", "spodnie"));
        p6.setImgUrl("/resources/img/jeansy_kek101f8d38-50b1-4140-b728-b63cc4ff4a61.jpg");
        p6.setName("Levi's");
        p6.setPrice(new BigDecimal(99.99));
        p6.getSizeToAmountMap().put("34", 25);
        p6.getSizeToAmountMap().put("36", 25);
        p6.getSizeToAmountMap().put("38", 30);
        p6.getSizeToAmountMap().put("40", 27);
        p6.getSizeToAmountMap().put("42", 19);
        p6.setDescription(description);

        /* Produkt 7 */
        p7 = new Product();
        description.setName("Opis");
        description.setValue("Jednoczęściowy strój kąpielowy z roślinnym motywem");
        p7.setCategory(getCategoryByName("kobieta"));
        p7.setImgUrl("/resources/img/stroj_kapielowyb8e05977-a0c5-4bdf-a0d0-5e9c529148de.jpg");
        p7.setName("O'Nell");
        p7.setPrice(new BigDecimal(79.99));
        p7.getSizeToAmountMap().put("34", 12);
        p7.getSizeToAmountMap().put("36", 22);
        p7.getSizeToAmountMap().put("38", 31);
        p7.getSizeToAmountMap().put("40", 26);
        p7.getSizeToAmountMap().put("42", 30);
        p7.setDescription(description);

        /* Produkt 8 */
        p8 = new Product();
        description.setName("Opis");
        description.setValue("Piżama z nadrukiem.");
        p8.setCategory(getCategoryByName("kobieta"));
        p8.setImgUrl("/resources/img/pizama_z_nadrukiem8de63bc8-9568-4786-9a28-84d8742c332b.jpg");
        p8.setName("OYSHO");
        p8.setPrice(new BigDecimal(79.99));
        p8.getSizeToAmountMap().put("S", 27);
        p8.getSizeToAmountMap().put("M", 31);
        p8.getSizeToAmountMap().put("L", 14);
        p8.getSizeToAmountMap().put("XL", 10);
        p8.getSizeToAmountMap().put("XXL", 20);
        p8.setDescription(description);

        /* Produkt 9 */
        p9 = new Product();
        description.setName("Opis");
        description.setValue("Kurtka z nadrukiem BOHO z motywami roslinnymi.");
        p9.setCategory(getCategoryByName("dzieciece", "5 - 14 lata"));
        p9.setImgUrl("/resources/img/kurtka_wiatrowka_dzieckode5ed809-5f72-45b2-8750-3b0135b81e4f.jpg");
        p9.setName("mint&berry");
        p9.setPrice(new BigDecimal(69.99));
        p9.getSizeToAmountMap().put("92", 26);
        p9.getSizeToAmountMap().put("98", 30);
        p9.getSizeToAmountMap().put("104", 30);
        p9.getSizeToAmountMap().put("110", 21);
        p9.getSizeToAmountMap().put("116", 20);
        p9.getSizeToAmountMap().put("122", 30);
        p9.getSizeToAmountMap().put("128", 35);
        p9.setDescription(description);

        /* Produkt 10 */
        p10 = new Product();
        description.setName("Opis");
        description.setValue("Torba na ramię.");
        p10.setCategory(getCategoryByName("kobieta"));
        p10.setImgUrl("/resources/img/torba_na_ramie2a11992e-17e9-4b30-b678-098deed70ced.jpg");
        p10.setName("RIVER ISLAND");
        p10.setPrice(new BigDecimal(149.00));
        p10.getSizeToAmountMap().put("one-size", 50);
        p10.setDescription(description);

        /* Produkt 11 */
        p11 = new Product();
        description.setName("Opis");
        description.setValue("Spódnica z zakładką.");
        p11.setCategory(getCategoryByName("kobieta", "spodnice", "mini"));
        p11.setImgUrl("/resources/img/spodnica_z_zakladka91801497-3182-4177-b364-ae1ece89c2db.jpg");
        p11.setName("New Look");
        p11.setPrice(new BigDecimal(84.00));
        p11.getSizeToAmountMap().put("34", 20);
        p11.getSizeToAmountMap().put("36", 30);
        p11.getSizeToAmountMap().put("38", 40);
        p11.getSizeToAmountMap().put("40", 40);
        p11.getSizeToAmountMap().put("42", 40);
        p11.getSizeToAmountMap().put("44", 40);
        p11.getSizeToAmountMap().put("46", 40);
        p11.setDescription(description);

        /* Produkt 12 */
        p12 = new Product();
        description.setName("Opis");
        description.setValue("Portfel skórzany czarny.");
        p12.setCategory(getCategoryByName("mezczyzna"));
        p12.setImgUrl("/resources/img/portfel4777680d-44ca-44bd-8631-b92065118023.jpg");
        p12.setName("Calvin Klein");
        p12.setPrice(new BigDecimal(249.00));
        p12.getSizeToAmountMap().put("one-size", 40);
        p12.setDescription(description);

        /* Produkt 13 */
        p13 = new Product();
        description.setName("Opis");
        description.setValue("Bluza z kapturem z kieszenią z przodu.");
        p13.setCategory(getCategoryByName("mezczyzna", "swetry"));
        p13.setImgUrl("/resources/img/bluza_z_kapturem588a8838-be54-4f59-ba9c-3cb5bc5d7f79.jpg");
        p13.setName("Tommy Hilfiger");
        p13.setPrice(new BigDecimal(419.00));
        p13.getSizeToAmountMap().put("S", 25);
        p13.getSizeToAmountMap().put("M", 35);
        p13.getSizeToAmountMap().put("L", 40);
        p13.getSizeToAmountMap().put("XL", 20);
        p13.getSizeToAmountMap().put("XXL", 15);
        p13.setDescription(description);

        /* Produkt 14 */
        p14 = new Product();
        description.setName("Opis");
        description.setValue("Jeansowa koszula z kołnierzykiem.");
        p14.setCategory(getCategoryByName("mezczyzna", "koszule/koszulki", "koszule"));
        p14.setImgUrl("/resources/img/koszulac98ef56f-717f-47c0-b5af-a094122c75c4.jpg");
        p14.setName("Lee");
        p14.setPrice(new BigDecimal(309.00));
        p14.getSizeToAmountMap().put("S", 25);
        p14.getSizeToAmountMap().put("M", 30);
        p14.getSizeToAmountMap().put("L", 30);
        p14.getSizeToAmountMap().put("XL", 30);
        p14.getSizeToAmountMap().put("XXL", 22);
        p14.setDescription(description);

        /* Produkt 15 */
        p15 = new Product();
        description.setName("Opis");
        description.setValue("Trampki Superstar.");
        p15.setCategory(getCategoryByName("kobieta"));
        p15.setImgUrl("/resources/img/trampki1f025cef-5a49-4ed4-bd7c-608bfa4d70bd.jpg");
        p15.setName("Adidas");
        p15.setPrice(new BigDecimal(399.00));
        p15.getSizeToAmountMap().put("36", 20);
        p15.getSizeToAmountMap().put("36 2/3", 20);
        p15.getSizeToAmountMap().put("37 1/3", 20);
        p15.getSizeToAmountMap().put("38", 20);
        p15.getSizeToAmountMap().put("38 2/3", 20);
        p15.getSizeToAmountMap().put("39 1/3", 20);
        p15.getSizeToAmountMap().put("40", 20);
        p15.getSizeToAmountMap().put("40 2/3", 20);
        p15.getSizeToAmountMap().put("41 1/3", 20);
        p15.getSizeToAmountMap().put("42", 10);
        p15.getSizeToAmountMap().put("42 2/3", 15);
        p15.getSizeToAmountMap().put("43 1/3", 10);
        p15.setDescription(description);


        /* Fulfill list */
        products.addAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15));

        /* Return list of products */
        return products;
    }

    private Category getCategoryByName(String... categoryNames) {
        if(categoryNames.length < 1) {
            return null;
        }
        return getCategoryByNameRec(categories, categoryNames);
    }

    private Category getCategoryByNameRec(List<Category> currentCategories, String... categoryNames) {
        if(categoryNames.length < 1) {
            return null;
        } else if(categoryNames.length > 1) {
            return getCategoryByNameRec(currentCategories.stream()
                                                         .filter(category -> category.getName()
                                                                                     .equalsIgnoreCase(categoryNames[0]))
                                                         .findFirst()
                                                         .map(Category::getSubcategories)
                                                         .orElse(new ArrayList<>()),
                                        Arrays.copyOfRange(categoryNames, 1, categoryNames.length));
        }

        return currentCategories.stream()
                                .filter(category -> category.getName().equalsIgnoreCase(categoryNames[0]))
                                .findFirst()
                                .orElse(null);
    }
}
