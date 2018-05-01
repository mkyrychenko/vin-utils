import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VinUtilsTest {
    @Test
    public void shouldSuccessOnCorrectVin() {
        final String correctVin = "2G1WB5E37E1110567";

        assertTrue("VIN " + correctVin + " is correct, but validation failed.", VinUtils.isValidVin(correctVin));
    }

    @Test
    public void sholdGenerateCorrectVin() {
        final String generated = VinUtils.getRandomVin();

        assertTrue("VIN " + generated + " was generated, but validation failed.", VinUtils.isValidVin(generated));
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnNullVinValidation() throws Exception {
        VinUtils.validateVin(null);
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnEmptyVinValidation() throws Exception {
        VinUtils.validateVin("");
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnBlankVinValidation() throws Exception {
        VinUtils.validateVin("     ");
    }

    /**
     * VIN may not contain I, O or Q
     *
     * @throws Exception as expected
     */
    @Test(expected = InvalidVinException.class)
    public void shouldFailOnWrongVinValidation() throws Exception {
        VinUtils.validateVin("2G1WB5Q37E1110567");
    }

    @Test
    public void shouldContainFailReasonOnWrongVinValidation() {
        final String vin = "2G1WB5E37EI110567";

        try {
            VinUtils.validateVin(vin);
        } catch (InvalidVinException e) {
            assertEquals("Wrong VIN in exception should equal specified one", vin, e.getWrongVin());
            assertEquals("Error message were not found", "Illegal character 'I' in VIN '2G1WB5E37EI110567' at position 10", e.getMessage());
        }
    }

    @Test
    public void shouldContainFailReasonOnTooLongVinValidation() {
        final String vin = "2G1WB5E37EI1105673";

        try {
            VinUtils.validateVin(vin);
        } catch (InvalidVinException e) {
            assertEquals("Wrong VIN in exception should equal specified one", vin, e.getWrongVin());
            assertEquals("Error message were not found", "Length of VIN (without possible additional characters) should equal 17", e.getMessage());
        }
    }

    @Test
    public void shouldContainFailReasonOnWrongCheckDigitValidation() {
        final String vin = "2G1WB5E36E1110567";

        try {
            VinUtils.validateVin(vin);
        } catch (InvalidVinException e) {
            assertEquals("Wrong VIN in exception should equal specified one", vin, e.getWrongVin());
            assertEquals("Error message were not found", "Illegal check digit '6' for VIN '2G1WB5E37E1110567'", e.getMessage());
        }
    }

    @Test
    public void shouldContainFailReasonOnNullVinValidation() {
        try {
            VinUtils.validateVin(null);
        } catch (InvalidVinException e) {
            assertNull("Wrong VIN in exception should be null", e.getWrongVin());
            assertEquals("Error message were not found", "VIN should not be null", e.getMessage());
        }
    }

    @Test
    public void shouldSuccessOnVinWithOtherChars() {
        final String correctVin = "2G1-WB5E3 7 E-1-110567";

        assertTrue("VIN " + correctVin + " is correct, but validation failed.", VinUtils.isValidVin(correctVin));
    }
}