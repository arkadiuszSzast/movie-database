package com.movie.database.movie_database.user.avatar.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AvatarValidatorImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AvatarValidator {
    String message() default "Invalid avatar";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
