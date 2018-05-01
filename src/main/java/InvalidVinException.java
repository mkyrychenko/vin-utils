/*
 * MAN CONFIDENTIAL
 * _______________________________
 *
 * Copyright © MAN Truck & Bus AG 2018 - 2018
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of MAN Truck & Bus AG and its suppliers, if any.
 * The intellectual and technical concepts contained herein are
 * proprietary to MAN Truck & Bus AG and its suppliers
 * and may be covered by patents or patents in process,
 * and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from MAN Truck & Bus AG.
 */

public class InvalidVinException extends Exception {

    private final String wrongVin;

    public InvalidVinException(final String wrongVin) {
        this.wrongVin = wrongVin;
    }

    public InvalidVinException(final String wrongVin,
                               final String message) {
        super(message);

        this.wrongVin = wrongVin;
    }

    public InvalidVinException(final String wrongVin,
                               final String message,
                               final Throwable cause) {
        super(message, cause);

        this.wrongVin = wrongVin;
    }

    public InvalidVinException(final String wrongVin,
                               final Throwable cause) {
        super(cause);

        this.wrongVin = wrongVin;
    }

    public String getWrongVin() {
        return this.wrongVin;
    }
}