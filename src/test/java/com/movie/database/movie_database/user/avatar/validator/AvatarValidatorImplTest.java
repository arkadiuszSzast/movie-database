package com.movie.database.movie_database.user.avatar.validator;

import com.movie.database.movie_database.support.exceptions.InvalidExtensionException;
import com.movie.database.movie_database.support.predicate.ExtensionAllowedPredicate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AvatarValidatorImplTest {

    @Mock
    private ExtensionAllowedPredicate<String> extensionAllowedPredicate;
    @Mock
    private ConstraintValidatorContext constraintValidatorContext;
    @InjectMocks
    private AvatarValidatorImpl avatarValidator;

    @Test
    @DisplayName("Should return true when extension is allowed")
    public void shouldReturnTrueWhenExtensionIsAllowed() throws IOException {
        //arrange
        var fileName = "filename.png";
        var file = new MockMultipartFile(fileName, fileName, "image/png",
                this.getClass().getResourceAsStream(fileName));
        when(extensionAllowedPredicate.negate()).thenReturn(extensionAllowedPredicate);
        when(extensionAllowedPredicate.test(anyString())).thenReturn(false);

        //act
        var result = avatarValidator.isValid(file, constraintValidatorContext);

        //assert
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when extension is not allowed")
    public void shouldReturnFalseWhenExtensionIsNotAllowed() throws IOException {
        //arrange
        var fileName = "filename.sh";
        var file = new MockMultipartFile(fileName, fileName, "image/png",
                this.getClass().getResourceAsStream(fileName));
        when(extensionAllowedPredicate.negate()).thenReturn(extensionAllowedPredicate);
        when(extensionAllowedPredicate.test(anyString())).thenReturn(true);

        //act && assert
        assertThrows(InvalidExtensionException.class, () -> avatarValidator.isValid(file, constraintValidatorContext));
    }
}