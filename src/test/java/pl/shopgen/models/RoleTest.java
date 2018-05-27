package pl.shopgen.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class RoleTest {

    @Test
    public void equalsContractTest() {
        EqualsVerifier.forClass(Sale.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
