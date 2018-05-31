package pl.shopgen.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class AddressTest {

    @Test
    public void equalsContractTest() {
        EqualsVerifier.forClass(Address.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
