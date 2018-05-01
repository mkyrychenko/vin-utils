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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VinValidator implements ConstraintValidator<VIN, String> {
    @Override
    public void initialize(final VIN constraintAnnotation) {
        // do nothing
    }

    @Override
    public boolean isValid(final String value,
                           final ConstraintValidatorContext context) {
        try {
            return VinUtils.validateVin(value);
        } catch (InvalidVinException e) {
            String errorMessage = e.getMessage();

            if (errorMessage == null || errorMessage.trim().length() == 0) {
                errorMessage = "";
            } else {
                errorMessage = " " + errorMessage;
            }

            errorMessage = "Provided VIN '" + e.getWrongVin() + "' is incorrect." + errorMessage;
            context
                    .buildConstraintViolationWithTemplate(errorMessage)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }

        return false;
    }
}