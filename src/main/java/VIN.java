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

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({
    ElementType.TYPE,
    ElementType.FIELD,
    ElementType.METHOD,
    ElementType.PARAMETER,
    ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VinValidator.class)
@ReportAsSingleViolation
@Documented
@SuppressWarnings("unused")
public @interface VIN {

    String message() default "Invalid VIN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}