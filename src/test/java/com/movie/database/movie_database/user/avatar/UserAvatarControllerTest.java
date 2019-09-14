package com.movie.database.movie_database.user.avatar;

import com.movie.database.movie_database.support.properties.FileStorageProperties;
import com.movie.database.movie_database.user.domain.ApplicationUserRepository;
import com.movie.database.movie_database.utils.LogInProvider;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.UserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MovieDbIntegrationTest
class UserAvatarControllerTest {

    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private UserProvider userProvider;
    @Autowired
    private ApplicationUserRepository applicationUserRepository;
    @MockBean
    private FileStorageProperties fileStorageProperties;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void mockDirectory(@TempDir Path tempDir) {
        var avatarsDirectory = tempDir.toAbsolutePath().toString();
        when(fileStorageProperties.getLocation()).thenReturn(avatarsDirectory);
        when(fileStorageProperties.getAllowedExtensions()).thenReturn(List.of("jpg", "png"));
    }

    @Test
    @DisplayName("Should save avatar as user")
    public void shouldSaveAvatarAsUser() throws IOException {
        var avatar = IOUtils.toByteArray(getClass().getResourceAsStream("/images/avatar.png"));
        var user = userProvider.createActivatedUserWithUserRole();
        var authToken = logInProvider.logIn(user);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .multiPart("file", "avatar.png", avatar)
                .post("/api/user/avatar")
                .then()
                .statusCode(204);

        user = applicationUserRepository.findById(user.getId()).get();
        var pathToAvatar = Path.of(fileStorageProperties.getLocation()).resolve(user.getAvatarUrl().get());
        assertThat(user.getAvatarUrl()).isPresent();
        assertThat(Files.exists(pathToAvatar)).isTrue();
    }

    @Test
    @DisplayName("Should save avatar as admin")
    public void shouldSaveAvatarAsAdmin() throws IOException {
        var avatar = IOUtils.toByteArray(getClass().getResourceAsStream("/images/avatar.png"));
        var user = userProvider.createActivatedUserWithAdminRole();
        var authToken = logInProvider.logIn(user);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .multiPart("file", "avatar.png", avatar)
                .post("/api/user/avatar")
                .then()
                .statusCode(204);

        user = applicationUserRepository.findById(user.getId()).get();
        var pathToAvatar = Path.of(fileStorageProperties.getLocation()).resolve(user.getAvatarUrl().get());
        assertThat(user.getAvatarUrl()).isPresent();
        assertThat(Files.exists(pathToAvatar)).isTrue();
    }

    @Test
    @DisplayName("Should not save avatar when user is not logged in")
    public void shouldNotSaveAvatarWhenUserIsNotLoggedIn() throws IOException {
        var avatar = IOUtils.toByteArray(getClass().getResourceAsStream("/images/avatar.png"));

        given()
                .port(port)
                .when()
                .multiPart("file", "avatar.png", avatar)
                .post("/api/user/avatar")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should return 400 when avatar is too big")
    public void shouldReturn400WhenAvatarIsTooBig() throws IOException {
        var avatar = IOUtils.toByteArray(getClass().getResourceAsStream("/images/avatar_too_big.png"));
        var user = userProvider.createActivatedUserWithUserRole();
        var authToken = logInProvider.logIn(user);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .multiPart("file", "avatar.png", avatar)
                .post("/api/user/avatar")
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Should return 400 when extension is not allowed")
    public void shouldReturn400WhenExtensionIsNotAllowed() throws IOException {
        var avatar = IOUtils.toByteArray(getClass().getResourceAsStream("/images/script.sh"));
        var user = userProvider.createActivatedUserWithUserRole();
        var authToken = logInProvider.logIn(user);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .multiPart("file", "script.sh", avatar)
                .post("/api/user/avatar")
                .then()
                .statusCode(400);
    }
}