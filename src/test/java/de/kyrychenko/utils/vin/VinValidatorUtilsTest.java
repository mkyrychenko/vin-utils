/*
 * MIT License
 *
 * Copyright (c) 2018 Mykhailo Kyrychenko <mykhailo.kyrychenko@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package de.kyrychenko.utils.vin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class VinValidatorUtilsTest {
    @Test
    public void shouldSuccessOnCorrectVin() {
        final String correctVin = "2G1WB5E37E1110567";

        assertTrue("VIN " + correctVin + " is correct, but validation failed.", VinValidatorUtils.isValidVin(correctVin));
    }

    @Test
    public void sholdValidateWrongVin() {
        final String vin = "xyz";

        assertFalse("Wrong VIN is provided, but validation succeed.", VinValidatorUtils.isValidVin(vin));
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnNullVinValidation() throws Exception {
        VinValidatorUtils.validateVin(null);
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnEmptyVinValidation() throws Exception {
        VinValidatorUtils.validateVin("");
    }

    @Test(expected = InvalidVinException.class)
    public void shouldFailOnBlankVinValidation() throws Exception {
        VinValidatorUtils.validateVin("     ");
    }

    /**
     * VIN may not contain I, O or Q
     *
     * @throws Exception as expected
     */
    @Test(expected = InvalidVinException.class)
    public void shouldFailOnWrongVinValidation() throws Exception {
        VinValidatorUtils.validateVin("2G1WB5Q37E1110567");
    }

    @Test
    public void shouldContainFailReasonOnWrongVinValidation() {
        final String vin = "2G1WB5E37EI110567";

        try {
            VinValidatorUtils.validateVin(vin);
        } catch (InvalidVinException e) {
            assertEquals("Wrong VIN in exception should equal specified one", vin, e.getWrongVin());
            assertEquals("Error message were not found", "Illegal character 'I' in VIN '2G1WB5E37EI110567' at position 10", e.getMessage());
        }
    }

    @Test
    public void shouldContainFailReasonOnTooLongVinValidation() {
        final String vin = "2G1WB5E37EI1105673";

        try {
            VinValidatorUtils.validateVin(vin);
        } catch (InvalidVinException e) {
            assertEquals("Wrong VIN in exception should equal specified one", vin, e.getWrongVin());
            assertEquals("Error message were not found", "Length of VIN (without possible additional characters) should equal 17", e.getMessage());
        }
    }

    @Test
    public void shouldContainFailReasonOnWrongCheckDigitValidation() {
        final String vin = "2G1WB5E36E1110567";

        try {
            VinValidatorUtils.validateVin(vin);
        } catch (InvalidVinException e) {
            assertEquals("Wrong VIN in exception should equal specified one", vin, e.getWrongVin());
            assertEquals("Error message were not found", "Illegal check digit '6' for VIN '2G1WB5E37E1110567'", e.getMessage());
        }
    }

    @Test
    public void shouldContainFailReasonOnNullVinValidation() {
        try {
            VinValidatorUtils.validateVin(null);
        } catch (InvalidVinException e) {
            assertNull("Wrong VIN in exception should be null", e.getWrongVin());
            assertEquals("Error message were not found", "VIN should not be null", e.getMessage());
        }
    }

    @Test
    public void shouldSuccessOnVinWithOtherChars() {
        final String correctVin = "2G1-WB5E3 7 E-1-110567";

        assertTrue("VIN " + correctVin + " is correct, but validation failed.", VinValidatorUtils.isValidVin(correctVin));
    }
}