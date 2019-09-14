package com.movie.database.movie_database.user.avatar.validator;

import com.movie.database.movie_database.support.exceptions.InvalidExtensionException;
import com.movie.database.movie_database.support.predicate.ExtensionAllowedPredicate;
import com.movie.database.movie_database.support.predicate.PredicateHelper;
import liquibase.util.file.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AvatarValidatorImpl implements ConstraintValidator<AvatarValidator, MultipartFile> {

    private final ExtensionAllowedPredicate<String> extensionAllowedPredicate;

    public AvatarValidatorImpl(ExtensionAllowedPredicate<String> extensionAllowedPredicate) {
        this.extensionAllowedPredicate = extensionAllowedPredicate;
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        var fileExtension = FilenameUtils.getExtension(value.getOriginalFilename());
        PredicateHelper.of(extensionAllowedPredicate)
                .throwIfFalse(fileExtension, InvalidExtensionException::new);
        return true;
    }
}
