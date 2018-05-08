package pl.shopgen.models.test;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.shopgen.models.Category;
import pl.shopgen.models.CategoryRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class CategoryDbSeeder implements CommandLineRunner {
    private CategoryRepository categoryRepository;

    public CategoryDbSeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... strings) {

        Category tshirt_k = new Category("t-shirt", Collections.emptyList());

        Category koszule_k = new Category("koszule", Collections.emptyList());

        Category top_k = new Category("top", Collections.emptyList());

        Category bluzki_k = new Category("bluzki", Collections.emptyList());

        Category koszule_sukienki_k = new Category("koszule/sukienki",
                Arrays.asList(tshirt_k, koszule_k, top_k, bluzki_k));

        Category koktajlowe_k = new Category("koktajlowe", Collections.emptyList());

        Category wieczorowe_k = new Category("wieczorowe", Collections.emptyList());

        Category sukienki_k = new Category("sukienki", Arrays.asList(wieczorowe_k, koktajlowe_k));

        Category mini_k = new Category("mini", Collections.emptyList());

        Category maxi_k = new Category("maxi", Collections.emptyList());
        Category spodnice_k = new Category("spodnice", Arrays.asList(mini_k, maxi_k));

        Category spodnie_k = new Category("spodnie", Collections.emptyList());

        Category kobieta_k = new Category("kobieta", Arrays.asList(koszule_sukienki_k, spodnie_k, sukienki_k, spodnice_k));
        /////////
        Category tshirt_m = new Category("t-shirt", Collections.emptyList());

        Category koszule_m = new Category("koszule", Collections.emptyList());

        Category koszulki_polo_m = new Category("koszulki polo", Collections.emptyList());

        Category koszule_koszulki_m = new Category("koszule/koszulki",
                Arrays.asList(tshirt_m, koszulki_polo_m, koszule_m));

        Category jeansy_m = new Category("jeansy", Collections.emptyList());

        Category bermudy_m = new Category("bermudy", Collections.emptyList());

        Category spodnie_m = new Category("spodnie", Arrays.asList(jeansy_m, bermudy_m));

        Category swetry_m = new Category("swetry", Collections.emptyList());

        Category marynarki_m = new Category("marynarki", Collections.emptyList());

        Category mezczyzna_m = new Category("mezczyzna",
                Arrays.asList(marynarki_m, swetry_m, spodnie_m, koszule_koszulki_m));
        ///////////
        Category od_zera_do_trzech_miesiecy_d = new Category("0 - 3 miesiece", Collections.emptyList());
        Category od_czterech_do_dwunastu_miesiecy_d = new Category("4 - 12 miesiece", Collections.emptyList());
        Category od_roku_do_czterech_lat_d = new Category("1 - 4 lata", Collections.emptyList());
        Category od_pieciu_do_czternastu_lat_d = new Category("5 - 14 lata", Collections.emptyList());
        Category dziewczynka_d = new Category("dziewczynka",
                Arrays.asList(od_zera_do_trzech_miesiecy_d,
                        od_czterech_do_dwunastu_miesiecy_d,
                        od_roku_do_czterech_lat_d,
                        od_pieciu_do_czternastu_lat_d));
        //////////////chlopiec
        Category od_zera_do_trzech_miesiecy_c = new Category("0 - 3 miesiece", Collections.emptyList());
        Category od_czterech_do_dwunastu_miesiecy_c = new Category("4 - 12 miesiece", Collections.emptyList());
        Category od_roku_do_czterech_lat_c = new Category("1 - 4 lata", Collections.emptyList());
        Category od_pieciu_do_czternastu_lat_c = new Category("5 - 14 lata", Collections.emptyList());
        Category chlopiec_c = new Category("chlopiec",
                Arrays.asList(od_zera_do_trzech_miesiecy_c,
                        od_czterech_do_dwunastu_miesiecy_c,
                        od_roku_do_czterech_lat_c,
                        od_pieciu_do_czternastu_lat_c));
        ///////////////////buty
        Category chlopiece_c = new Category("chlopiece", Collections.emptyList());
        Category dziewczece_d = new Category("dziewczece", Collections.emptyList());
        Category buty_d = new Category("buty", Arrays.asList(chlopiece_c, dziewczece_d));

        Category dziecko_d = new Category("dziecko", Arrays.asList(dziewczynka_d, chlopiec_c, buty_d));

        // drop all
        categoryRepository.deleteAll();

        //add data to the database
        List<Category> category = Arrays.asList(
                kobieta_k,
                mezczyzna_m,
                dziecko_d);
        for (Category data : category) {
            categoryRepository.save(data);
        }

    }
}
