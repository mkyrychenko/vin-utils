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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Utility class providing help methods to generate and validate
 * vehicle identification number (VIN)
 * <p>
 * Based on script placed in GitHub
 * <a href="https://github.com/yanigisawa/VinGenerator">VinGenerator</a> project<br>
 * and partly improved with algorithm of
 * <a href="https://introcs.cs.princeton.edu/java/31datatype/VIN.java.html">princeton university</a>
 */
public class VinUtils {
    private static final Random RANDOM = new Random();
    private static final String PREFIXES_FILE_NAME = "vin-prefixes.txt";
    private static final String ALLOWED_CHARS = "0123456789ABCDEFGHJKLMNPRSTUVWXYZ";
    private static final int[] VIN_POSITION_WEIGHT = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] VIN_LETTER_VALUE = {1, 2, 3, 4, 5, 6, 7, 8, 0, 1, 2, 3, 4, 5, 0, 7, 0, 9, 2, 3, 4, 5, 6, 7, 8, 9};

    private VinUtils() {
        throw new RuntimeException("Utility class should not be initialized");
    }

    /**
     * Generate random VIN
     *
     * @return randomly generated VIN
     */
    public static String getRandomVin() {
        final VinYear vinYear = getRandomVinStart();
        final StringBuilder vinBuilder = new StringBuilder();

        vinBuilder.append(vinYear.getFirst8());
        vinBuilder.append(getRandomVinChar());
        vinBuilder.append(vinYear.getYear());

        for (int i = 0; i < 7; i++) {
            vinBuilder.append(getRandomVinChar());
        }

        final String vin = vinBuilder.toString();

        try {
            return vin.substring(0, 8) + (int) getCheckSumChar(vin) + vin.substring(9);
        } catch (InvalidVinException ignored) {
            // should never happen, because here we calculate checksum for VIN generated by us
            // but checksum calculation could throw exception for validation of foreign VIN
            return null;
        }
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
        if (vin == null) {
            throw new InvalidVinException(null, "VIN should not be null");
        }

        final String validate = vin.toUpperCase().replaceAll("[^A-Z0-9]", "");

        if (validate.length() != 17) {
            throw new InvalidVinException(validate, "Length of VIN (without possible additional characters) should equal 17");
        }

        final int sum = getCheckSum(validate);
        final char check = validate.charAt(8);

        if (check != 'X' && (check < '0' || check > '9')) {
            throw new InvalidVinException(validate, "Illegal check digit '" + check + "' for VIN '" + validate + "'");
        }

        return (sum == 10 && check == 'X') || (sum == check - '0');
    }

    private static int getCheckSum(final String vin) throws InvalidVinException {
        int sum = 0;

        for (int i = 0; i < 17; i++) {
            final char key = vin.charAt(i);

            int value = 0;
            if (key >= 'A' && key <= 'Z') {
                value = VIN_LETTER_VALUE[key - 'A'];

                if (value == 0) {
                    throw new InvalidVinException(vin, String.format("Illegal character '%s' in VIN '%s' at position %d", key, vin, i));
                }
            } else if (key >= '0' && key <= '9') {
                value = Character.getNumericValue(key);
            } else {
                throw new InvalidVinException(vin, String.format("Illegal character '%s' in VIN '%s' at position %d", key, vin, i));
            }

            sum += value * VIN_POSITION_WEIGHT[i];
        }

        return sum % 11;
    }

    /**
     * Generate the VIN's checksum
     *
     * @param vin a VIN to get the checksum for
     * @return checksum for provided VIN
     */
    private static char getCheckSumChar(final String vin) throws InvalidVinException {
        final int checkSum = getCheckSum(vin);

        char checkChar = (char) checkSum;

        if (checkSum == 10) {
            checkChar = 'X';
        }

        return checkChar;
    }

    private static char getRandomVinChar() {
        // '33' is length of ALLOWED_CHARS
        return ALLOWED_CHARS.charAt(RANDOM.nextInt(33));
    }

    private static VinYear getRandomVinStart() {
        // '62178' is a number of lines in the file; + 1 to avoid reading of first line
        final int lineToRead = RANDOM.nextInt(62177) + 1;

        try (final Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource(PREFIXES_FILE_NAME).toURI()))) {
            final String line = stream.skip(lineToRead)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Problem occurred while read line " + lineToRead + " from " + PREFIXES_FILE_NAME));
            final String[] fields = line.split(" {3}");

            return new VinYear(fields[0].trim(), fields[1].trim());
        } catch (URISyntaxException | IOException | SecurityException e) {
            throw new RuntimeException("Problem occurred while reading " + PREFIXES_FILE_NAME, e);
        }
    }

    /**
     * Helper class used internally to save parts of VIN
     */
    private static class VinYear {
        private final String first8;
        private final String year;

        VinYear(final String first8,
                final String year) {
            this.first8 = first8;
            this.year = year;
        }

        String getFirst8() {
            return this.first8;
        }

        String getYear() {
            return this.year;
        }
    }
}