package pl.shopgen.models.test;

import org.bson.types.ObjectId;
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
        tshirt_k.setId(new ObjectId().toString());

        Category koszule_k = new Category("koszule", Collections.emptyList());
        koszule_k.setId(new ObjectId().toString());

        Category top_k = new Category("top", Collections.emptyList());
        top_k.setId(new ObjectId().toString());

        Category bluzki_k = new Category("bluzki", Collections.emptyList());
        bluzki_k.setId(new ObjectId().toString());

        Category koszule_sukienki_k = new Category("koszule/sukienki",
                Arrays.asList(tshirt_k, koszule_k, top_k, bluzki_k));
        koszule_sukienki_k.setId(new ObjectId().toString());

        Category koktajlowe_k = new Category("koktajlowe", Collections.emptyList());
        koktajlowe_k.setId(new ObjectId().toString());

        Category wieczorowe_k = new Category("wieczorowe", Collections.emptyList());
        wieczorowe_k.setId(new ObjectId().toString());

        Category sukienki_k = new Category("sukienki", Arrays.asList(wieczorowe_k, koktajlowe_k));
        sukienki_k.setId(new ObjectId().toString());

        Category mini_k = new Category("mini", Collections.emptyList());
        mini_k.setId(new ObjectId().toString());

        Category maxi_k = new Category("maxi", Collections.emptyList());
        maxi_k.setId(new ObjectId().toString());

        Category spodnice_k = new Category("spodnice", Arrays.asList(mini_k, maxi_k));
        spodnice_k.setId(new ObjectId().toString());

        Category spodnie_k = new Category("spodnie", Collections.emptyList());
        spodnie_k.setId(new ObjectId().toString());

        Category kobieta_k = new Category("kobieta",
                Arrays.asList(koszule_sukienki_k, spodnie_k, sukienki_k, spodnice_k));


        /////////
        Category tshirt_m = new Category("t-shirt", Collections.emptyList());
        tshirt_m.setId(new ObjectId().toString());

        Category koszule_m = new Category("koszule", Collections.emptyList());
        koszule_m.setId(new ObjectId().toString());

        Category koszulki_polo_m = new Category("koszulki polo", Collections.emptyList());
        koszulki_polo_m.setId(new ObjectId().toString());

        Category koszule_koszulki_m = new Category("koszule/koszulki",
                Arrays.asList(tshirt_m, koszulki_polo_m, koszule_m));
        koszule_koszulki_m.setId(new ObjectId().toString());

        Category jeansy_m = new Category("jeansy", Collections.emptyList());
        jeansy_m.setId(new ObjectId().toString());

        Category bermudy_m = new Category("bermudy", Collections.emptyList());
        bermudy_m.setId(new ObjectId().toString());

        Category spodnie_m = new Category("spodnie", Arrays.asList(jeansy_m, bermudy_m));
        spodnie_m.setId(new ObjectId().toString());

        Category swetry_m = new Category("swetry", Collections.emptyList());
        swetry_m.setId(new ObjectId().toString());

        Category marynarki_m = new Category("marynarki", Collections.emptyList());
        marynarki_m.setId(new ObjectId().toString());

        Category mezczyzna_m = new Category("mezczyzna",
                Arrays.asList(marynarki_m, swetry_m, spodnie_m, koszule_koszulki_m));
        ///////////
        Category od_zera_do_trzech_miesiecy_d = new Category("0 - 3 miesiece", Collections.emptyList());
        od_zera_do_trzech_miesiecy_d.setId(new ObjectId().toString());

        Category od_czterech_do_dwunastu_miesiecy_d = new Category("4 - 12 miesiece", Collections.emptyList());
        od_czterech_do_dwunastu_miesiecy_d.setId(new ObjectId().toString());

        Category od_roku_do_czterech_lat_d = new Category("1 - 4 lata", Collections.emptyList());
        od_roku_do_czterech_lat_d.setId(new ObjectId().toString());

        Category od_pieciu_do_czternastu_lat_d = new Category("5 - 14 lata", Collections.emptyList());
        od_pieciu_do_czternastu_lat_d.setId(new ObjectId().toString());

        Category dziewczynka_d = new Category("dziewczynka",
                Arrays.asList(od_zera_do_trzech_miesiecy_d,
                        od_czterech_do_dwunastu_miesiecy_d,
                        od_roku_do_czterech_lat_d,
                        od_pieciu_do_czternastu_lat_d));
        dziewczynka_d.setId(new ObjectId().toString());

        //////////////chlopiec
        Category od_zera_do_trzech_miesiecy_c = new Category("0 - 3 miesiece", Collections.emptyList());
        od_zera_do_trzech_miesiecy_c.setId(new ObjectId().toString());

        Category od_czterech_do_dwunastu_miesiecy_c = new Category("4 - 12 miesiece", Collections.emptyList());
        od_czterech_do_dwunastu_miesiecy_c.setId(new ObjectId().toString());

        Category od_roku_do_czterech_lat_c = new Category("1 - 4 lata", Collections.emptyList());
        od_roku_do_czterech_lat_c.setId(new ObjectId().toString());

        Category od_pieciu_do_czternastu_lat_c = new Category("5 - 14 lata", Collections.emptyList());
        od_pieciu_do_czternastu_lat_c.setId(new ObjectId().toString());

        Category chlopiec_c = new Category("chlopiec",
                Arrays.asList(od_zera_do_trzech_miesiecy_c,
                        od_czterech_do_dwunastu_miesiecy_c,
                        od_roku_do_czterech_lat_c,
                        od_pieciu_do_czternastu_lat_c));
        chlopiec_c.setId(new ObjectId().toString());

        /////////////////// buty
        Category chlopiece_c = new Category("chlopiece", Collections.emptyList());
        chlopiece_c.setId(new ObjectId().toString());

        Category dziewczece_d = new Category("dziewczece", Collections.emptyList());
        dziewczece_d.setId(new ObjectId().toString());

        Category buty_d = new Category("buty", Arrays.asList(chlopiece_c, dziewczece_d));
        buty_d.setId(new ObjectId().toString());

        Category dziecko_d = new Category("dziecko", Arrays.asList(dziewczynka_d, chlopiec_c, buty_d));

        // drop all
        categoryRepository.deleteAll();

        //add data to the database
        List<Category> category = Arrays.asList(
                kobieta_k,
                mezczyzna_m,
                dziecko_d);
        categoryRepository.insert(category);
    }
}
