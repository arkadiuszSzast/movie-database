package com.movie.database.movie_database.support.recaptcha;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = RecaptchaConstraintValidator.class)
@Documented
public @interface RecaptchaValid {

    String message() default "Recaptcha invalid!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
