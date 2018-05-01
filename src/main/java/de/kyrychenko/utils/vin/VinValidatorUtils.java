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

/**
 * Utility class providing help methods to validate vehicle identification number (VIN)
 * <p>
 * Based on script placed in GitHub
 * <a href="https://github.com/yanigisawa/VinGenerator">VinGenerator</a> project<br>
 * and partly improved with algorithm of
 * <a href="https://introcs.cs.princeton.edu/java/31datatype/VIN.java.html">princeton university</a>
 */
public final class VinValidatorUtils {
    private static final int[] VIN_POSITION_WEIGHT = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] VIN_LETTER_VALUE = {1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3, 4, 5, 0, 7, 0, 9, 2, 3, 4, 5, 6, 7, 8, 9};

    private VinValidatorUtils() {
        throw new RuntimeException("Utility class should not be initialized");
    }

    /**
     * Check whether provided VIN is a valid one,
     * suppressing all errors in case of wrong vin.
     *
     * @param vin a string to validate
     * @return {@code true} if specified parameter is a valid VIN, {@code false} otherwise
     */
    public static boolean isValidVin(final String vin) {
        try {
            return validateVin(vin);
        } catch (InvalidVinException e) {
            return false;
        }
    }

    /**
     * Validate vehicle identification number (VIN)
     *
     * @param vin a {@link String} with VIN to validate
     * @return {@code true} if specified parameter is a valid VIN, {@code false} otherwise
     * @throws InvalidVinException if validation was failed
     */
    public static boolean validateVin(final String vin) throws InvalidVinException {
        final String normalized = normalizeVin(vin);
        final int sum = getVinCheckSum(normalized);
        final char check = normalized.charAt(8);

        if (check != 'X' && (check < '0' || check > '9')) {
            throw new InvalidVinException(normalized, "Illegal check digit '" + check + "' for VIN '" + normalized + "'");
        }

        return (sum == 10 && check == 'X') || (sum == check - '0');
    }

    /**
     * Calculate VIN checksum
     *
     * @param vin a {@link String} with VIN to checksum for
     * @return checksum of VIN
     * @throws InvalidVinException if calculation was failed
     */
    public static int getVinCheckSum(final String vin) throws InvalidVinException {
        final String normalized = normalizeVin(vin);

        int sum = 0;

        for (int i = 0; i < 17; i++) {
            final char key = normalized.charAt(i);

            int value = 0;
            if (key >= 'A' && key <= 'Z') {
                value = VIN_LETTER_VALUE[key - 'A'];

                if (value == 0) {
                    throw new InvalidVinException(normalized, String.format("Illegal character '%s' in VIN '%s' at position %d", key, normalized, i));
                }
            } else if (key >= '0' && key <= '9') {
                value = Character.getNumericValue(key);
            } else {
                throw new InvalidVinException(normalized, String.format("Illegal character '%s' in VIN '%s' at position %d", key, normalized, i));
            }

            sum += value * VIN_POSITION_WEIGHT[i];
        }

        return sum % 11;
    }

    /**
     * Calculate VIN checksum char
     *
     * @param vin a VIN to get the checksum for
     * @return checksum for provided VIN
     * @throws InvalidVinException if checksum calculation was failed
     */
    public static char getVinCheckSumChar(final String vin) throws InvalidVinException {
        final int checkSum = VinValidatorUtils.getVinCheckSum(vin);

        if (checkSum == 10) {
            return 'X';
        } else {
            return (char) (checkSum + '0');
        }
    }

    private static String normalizeVin(final String vin) throws InvalidVinException {
        if (vin == null) {
            throw new InvalidVinException(null, "VIN should not be null");
        }

        final String normalized = vin.toUpperCase().replaceAll("[^A-Z0-9]", "");

        if (normalized.length() != 17) {
            throw new InvalidVinException(normalized, "Length of VIN (without possible additional characters) should equal 17");
        }

        return normalized;
    }
}