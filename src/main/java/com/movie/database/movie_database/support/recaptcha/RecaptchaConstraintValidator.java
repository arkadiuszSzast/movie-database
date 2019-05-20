package com.movie.database.movie_database.support.recaptcha;

import com.github.mkopylec.recaptcha.validation.RecaptchaValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RecaptchaConstraintValidator implements ConstraintValidator<RecaptchaValid, String> {

    private final RecaptchaValidator recaptchaValidator;

    public RecaptchaConstraintValidator(RecaptchaValidator recaptchaValidator) {
        super();
        this.recaptchaValidator = recaptchaValidator;
    }

    @Override
    public void initialize(RecaptchaValid constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return recaptchaValidator.validate(value).isSuccess();
    }
}
