import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class VinUtilsTest {
    @Test
    public void shouldSuccessOnCorrectVin() {
        final String correctVin = "2G1WB5E37E1110567";

        assertTrue("VIN " + correctVin + " is correct, but validation failed.", VinUtils.isValidVin(correctVin));
    }

    public void sholdGenerateCorrectVin() {
        final String generated = VinUtils.getRandomVin();

        assertTrue("VIN " + generated + " was generated, but validation failed.", VinUtils.isValidVin(generated));
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnNullVinValidation() throws Exception {
        VinUtils.validateVin(null);
    }
}