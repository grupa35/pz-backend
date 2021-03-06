package pl.shopgen.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class UserTest {

    @Test
    public void equalContractTest() {
        EqualsVerifier.forClass(User.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}