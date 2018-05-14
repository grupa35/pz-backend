package pl.shopgen.models;


import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class SaleDTOTest {


    @Test
    public void equalsContractTest() {
        EqualsVerifier.forClass(SaleDTO.class)
                .suppress(Warning.STRICT_INHERITANCE)
                .suppress(Warning.NONFINAL_FIELDS)
                .verify();
    }
}