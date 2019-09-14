package com.movie.database.movie_database.user.avatar;

import com.movie.database.movie_database.support.properties.FileStorageProperties;
import com.movie.database.movie_database.user.ApplicationUserGetService;
import com.movie.database.movie_database.user.ApplicationUserUpdateService;
import com.movie.database.movie_database.user.domain.ApplicationUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAvatarServiceTest {

    @Mock
    private FileStorageProperties fileStorageProperties;
    @Mock
    private ApplicationUserGetService applicationUserGetService;
    @Mock
    private ApplicationUserUpdateService applicationUserUpdateService;
    @InjectMocks
    private UserAvatarService userAvatarService;


    @Test
    @DisplayName("Should save avatar when not exists")
    public void shouldSaveAvatarWhenNotExists(@TempDir Path tempDir) throws IOException {
        //arrange
        var avatarsDirectory = tempDir.toAbsolutePath().toString();
        var userId = UUID.randomUUID();
        var fileName = "filename";
        var user = new ApplicationUser("username", "password", "email");
        var avatar = new MockMultipartFile(fileName, fileName, "image/png",
                this.getClass().getResourceAsStream(fileName));
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(fileStorageProperties.getLocation()).thenReturn(avatarsDirectory);

        //act
        userAvatarService.save(avatar, userId);

        //assert
        assertThat(user.getAvatarUrl()).isPresent();
        assertThat(Files.exists(Path.of(avatarsDirectory).resolve(user.getAvatarUrl().get()))).isTrue();
        verify(applicationUserUpdateService, times(1)).update(user);
    }

    @Test
    @DisplayName("Should update avatar when already exists")
    public void shouldUpdateAvatarWhenAlreadyExists(@TempDir Path tempDir) throws IOException {
        //arrange
        var avatarsDirectory = tempDir.toAbsolutePath().toString();
        var userId = UUID.randomUUID();
        var fileName = "filename";
        var oldAvatarFilename = UUID.randomUUID().toString();
        var user = new ApplicationUser("username", "password", "email");
        user.setAvatarUrl(oldAvatarFilename);
        var pathToOldAvatar = tempDir.resolve(avatarsDirectory).resolve(user.getAvatarUrl().get());
        Files.createFile(pathToOldAvatar);
        var avatar = new MockMultipartFile(fileName, fileName, "image/png",
                this.getClass().getResourceAsStream(fileName));
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(fileStorageProperties.getLocation()).thenReturn(avatarsDirectory);

        //act
        userAvatarService.save(avatar, userId);

        //assert
        assertThat(user.getAvatarUrl()).isPresent();
        assertThat(Files.exists(pathToOldAvatar)).isFalse();
        assertThat(Files.exists(Path.of(avatarsDirectory).resolve(user.getAvatarUrl().get()))).isTrue();
        verify(applicationUserUpdateService, times(1)).update(user);
    }

    @Test
    @DisplayName("Should throw exception when cannot delete old avatar")
    public void shouldThrowExceptionWhenCannotDeleteOldAvatar(@TempDir Path tempDir) throws IOException {
        //arrange
        var avatarsDirectory = tempDir.toAbsolutePath().toString();
        var userId = UUID.randomUUID();
        var fileName = "filename";
        var user = new ApplicationUser("username", "password", "email");
        user.setAvatarUrl(tempDir.resolve(UUID.randomUUID().toString()).toString());
        Files.createFile(Paths.get(user.getAvatarUrl().get()));
        tempDir.toFile().setReadOnly();
        var avatar = new MockMultipartFile(fileName, fileName, "image/png",
                this.getClass().getResourceAsStream(fileName));
        when(applicationUserGetService.findById(userId)).thenReturn(user);
        when(fileStorageProperties.getLocation()).thenReturn(avatarsDirectory);

        //act && assert
        assertThrows(IOException.class, () -> userAvatarService.save(avatar, userId));
        verify(applicationUserUpdateService, never()).update(user);
        tempDir.toFile().setWritable(true);
    }
}