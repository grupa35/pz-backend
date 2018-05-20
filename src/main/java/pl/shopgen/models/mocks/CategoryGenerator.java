package pl.shopgen.models.mocks;

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

        Category s2 = new Category("koszule", Collections.emptyList());

        Category s3 = new Category("top", Collections.emptyList());

        Category s4 = new Category("bluzki", Collections.emptyList());

        Category s5 = new Category("koszule/sukienki",
                Arrays.asList(s1, s2, s3, s4));

        Category s6 = new Category("koktajlowe", Collections.emptyList());

        Category s7 = new Category("wieczorowe", Collections.emptyList());

        Category s8 = new Category("sukienki", Arrays.asList(s7, s6));

        Category s9 = new Category("mini", Collections.emptyList());

        Category s10 = new Category("maxi", Collections.emptyList());

        Category s11 = new Category("spodnice", Arrays.asList(s9, s10));

        Category s12 = new Category("spodnie", Collections.emptyList());

        c1 = new Category("kobieta",
                Arrays.asList(s5, s12, s8, s11));

        Category s13 = new Category("t-shirt", Collections.emptyList());

        Category s14 = new Category("koszule", Collections.emptyList());

        Category s15 = new Category("koszulki polo", Collections.emptyList());

        Category s16 = new Category("koszule/koszulki",
                Arrays.asList(s13, s14, s15));

        Category s17 = new Category("jeansy", Collections.emptyList());

        Category s18 = new Category("bermudy", Collections.emptyList());

        Category s19 = new Category("spodnie", Arrays.asList(s17, s18));

        Category s20 = new Category("swetry", Collections.emptyList());

        Category s21 = new Category("marynarki", Collections.emptyList());

        c2 = new Category("mezczyzna",
                Arrays.asList(s16, s19, s20, s21));

        Category s22 = new Category("0 - 3 miesiece", Collections.emptyList());

        Category s23 = new Category("4 - 12 miesiece", Collections.emptyList());

        Category s24 = new Category("1 - 4 lata", Collections.emptyList());

        Category s25 = new Category("5 - 14 lata", Collections.emptyList());

        Category s26 = new Category("dziewczynka",
                Arrays.asList(s22,
                        s23,
                        s24,
                        s25));

        Category s27 = new Category("0 - 3 miesiece", Collections.emptyList());

        Category s28 = new Category("4 - 12 miesiece", Collections.emptyList());

        Category s29 = new Category("1 - 4 lata", Collections.emptyList());

        Category s30 = new Category("5 - 14 lata", Collections.emptyList());

        Category s31 = new Category("chlopiec",
                Arrays.asList(s27,
                        s28,
                        s29,
                        s30));

        Category s32 = new Category("chlopiece", Collections.emptyList());

        Category s33 = new Category("dziewczece", Collections.emptyList());

        Category s34 = new Category("buty", Arrays.asList(s32, s33));

        c3 = new Category("dziecko", Arrays.asList(s26, s31, s34));

        categories.addAll(Arrays.asList(c1, c2, c3));

        return categories;
    }
}
