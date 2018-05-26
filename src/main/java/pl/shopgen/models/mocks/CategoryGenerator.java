package pl.shopgen.models.mocks;

import org.bson.types.ObjectId;
import pl.shopgen.models.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CategoryGenerator {

    private List<Category> categories = new ArrayList<>();

    private Category c1, c2, c3;

    public List<Category> generateCategories() {

        Category s1 = new Category("t-shirt", Collections.emptyList());
        s1.setId(new ObjectId().toString());

        Category s2 = new Category("koszule", Collections.emptyList());
        s2.setId(new ObjectId().toString());

        Category s3 = new Category("top", Collections.emptyList());
        s3.setId(new ObjectId().toString());

        Category s4 = new Category("bluzki", Collections.emptyList());
        s4.setId(new ObjectId().toString());

        Category s5 = new Category("koszule/sukienki",
                Arrays.asList(s1, s2, s3, s4));
        s5.setId(new ObjectId().toString());

        Category s6 = new Category("koktajlowe", Collections.emptyList());
        s6.setId(new ObjectId().toString());

        Category s7 = new Category("wieczorowe", Collections.emptyList());
        s7.setId(new ObjectId().toString());

        Category s8 = new Category("sukienki", Arrays.asList(s7, s6));
        s8.setId(new ObjectId().toString());

        Category s9 = new Category("mini", Collections.emptyList());
        s9.setId(new ObjectId().toString());

        Category s10 = new Category("maxi", Collections.emptyList());
        s10.setId(new ObjectId().toString());

        Category s11 = new Category("spodnice", Arrays.asList(s9, s10));
        s11.setId(new ObjectId().toString());

        Category s12 = new Category("spodnie", Collections.emptyList());
        s12.setId(new ObjectId().toString());

        c1 = new Category("kobieta",
                Arrays.asList(s5, s12, s8, s11));

        Category s13 = new Category("t-shirt", Collections.emptyList());
        s13.setId(new ObjectId().toString());

        Category s14 = new Category("koszule", Collections.emptyList());
        s14.setId(new ObjectId().toString());

        Category s15 = new Category("koszulki polo", Collections.emptyList());
        s15.setId(new ObjectId().toString());

        Category s16 = new Category("koszule/koszulki",
                Arrays.asList(s13, s14, s15));
        s16.setId(new ObjectId().toString());

        Category s17 = new Category("jeansy", Collections.emptyList());
        s17.setId(new ObjectId().toString());

        Category s18 = new Category("bermudy", Collections.emptyList());
        s18.setId(new ObjectId().toString());

        Category s19 = new Category("spodnie", Arrays.asList(s17, s18));
        s19.setId(new ObjectId().toString());

        Category s20 = new Category("swetry", Collections.emptyList());
        s20.setId(new ObjectId().toString());

        Category s21 = new Category("marynarki", Collections.emptyList());
        s21.setId(new ObjectId().toString());

        c2 = new Category("mezczyzna",
                Arrays.asList(s16, s19, s20, s21));

        Category s22 = new Category("0 - 3 miesiece", Collections.emptyList());
        s22.setId(new ObjectId().toString());

        Category s23 = new Category("4 - 12 miesiece", Collections.emptyList());
        s23.setId(new ObjectId().toString());

        Category s24 = new Category("1 - 4 lata", Collections.emptyList());
        s24.setId(new ObjectId().toString());

        Category s25 = new Category("5 - 14 lata", Collections.emptyList());
        s25.setId(new ObjectId().toString());

        Category s26 = new Category("dziewczynka",
                Arrays.asList(s22,
                        s23,
                        s24,
                        s25));
        s26.setId(new ObjectId().toString());


        Category s27 = new Category("0 - 3 miesiece", Collections.emptyList());
        s27.setId(new ObjectId().toString());

        Category s28 = new Category("4 - 12 miesiece", Collections.emptyList());
        s28.setId(new ObjectId().toString());

        Category s29 = new Category("1 - 4 lata", Collections.emptyList());
        s29.setId(new ObjectId().toString());

        Category s30 = new Category("5 - 14 lata", Collections.emptyList());
        s30.setId(new ObjectId().toString());

        Category s31 = new Category("chlopiec",
                Arrays.asList(s27,
                        s28,
                        s29,
                        s30));
        s31.setId(new ObjectId().toString());



        Category s32 = new Category("chlopiece", Collections.emptyList());
        s32.setId(new ObjectId().toString());


        Category s33 = new Category("dziewczece", Collections.emptyList());
        s32.setId(new ObjectId().toString());


        Category s34 = new Category("buty", Arrays.asList(s32, s33));
        s34.setId(new ObjectId().toString());


        c3 = new Category("dziecko", Arrays.asList(s26, s31, s34));

        categories.addAll(Arrays.asList(c1, c2, c3));

        return categories;
    }
}
