package pl.shopgen.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class ResponseStatusDTOTest {
    @Test
    public void equalsContractTest() {
        EqualsVerifier.forClass(ResponseStatusDTO.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}
