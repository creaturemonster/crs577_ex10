package com.rf.inventory.webapps;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Checks whether a given product ID value is valid.
 * Validation is done by the @NotNull, @Min, and @Max constraints on the
 * custom validation annotation. 
 */
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
// TODO: Add validation constraints to the custom validator annotation:
//       1. value must not be null
//       2. minimum value is 1
//       3. maximum value is 999999999
//       Define message keys for all messages. Add messages to 
//       config/ValidationMessages.properties
// HINT: See slide 10-18
@NotNull(message="{name.missing}")
@Size(min=1, max=999999999, message="{name.length.bad}")
public @interface ProductIdValidator {

    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
