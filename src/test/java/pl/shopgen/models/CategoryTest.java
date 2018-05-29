package pl.shopgen.models;


import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.util.AssertionErrors.assertEquals;


public class CategoryTest {
    private Category c1, c2, c3, c4, c5, c6, c7, c8, c9;

    @Before
    public void createSomeCategories() {
        c1 = new Category("a", Collections.emptyList());
        c1.setId(new ObjectId().toString());
        c2 = new Category("b", Collections.emptyList());
        c2.setId(new ObjectId().toString());
        c3 = new Category("c", Collections.emptyList());
        c3.setId(new ObjectId().toString());
        c4 = new Category("d", Collections.emptyList());
        c4.setId(new ObjectId().toString());
        c5 = new Category("e",
                Arrays.asList(c1, c3, c4));
        c5.setId(new ObjectId().toString());
        c6 = new Category("a", Collections.emptyList());
        c6.setId(new ObjectId().toString());
        c7 = new Category("b", Collections.emptyList());
        c7.setId(new ObjectId().toString());
        c8 = new Category("c", Collections.emptyList());
        c8.setId(new ObjectId().toString());
        c9 = new Category("d",
                Arrays.asList(c6, c8));
        c9.setId(new ObjectId().toString());
    }

    @Test
    public void equalsContractTest() {
        EqualsVerifier.forClass(Category.class)
                .withPrefabValues(Category.class, c1, c5)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }

    @Test
    public void getParentOfByIdTest() {

        assertEquals("Category cannot be subcategory of itself", null, c1.getParentOfById(c1.getId()));
        assertEquals("Subcategory cannot be null", null, null);
        assertEquals("Returns parentOf subcategory", c5, c5.getParentOfById(c4.getId()));
        assertEquals("There is no subcategory with this ID", null, c5.getParentOfById(c2.getId()));
    }

    @Test
    public void getSubCategoryByIdTest() {

        assertEquals("Category has no subcategories", null, c6.getSubCategoryById(c7.getId()));
        assertEquals("Subcategory cannot be null", null, null);
        assertEquals("Returns subcategory", c8, c9.getSubCategoryById(c8.getId()));
        assertEquals("There is no subcategory with this ID", null, c9.getSubCategoryById(c7.getId()));
    }
}

