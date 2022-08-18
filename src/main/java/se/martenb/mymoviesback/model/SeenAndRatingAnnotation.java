package se.martenb.mymoviesback.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = SeenAndRatingValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface SeenAndRatingAnnotation {
    String message() default "rating cannot be set if seen is not true and if seen is true then rating must be between 1 and 10";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
