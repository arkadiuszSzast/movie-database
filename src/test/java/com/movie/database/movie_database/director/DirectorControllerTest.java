package com.movie.database.movie_database.director;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.director.domain.Director;
import com.movie.database.movie_database.director.domain.DirectorRepository;
import com.movie.database.movie_database.utils.DirectorProvider;
import com.movie.database.movie_database.utils.LogInProvider;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
class DirectorControllerTest {

    @Autowired
    private DirectorProvider directorProvider;
    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private DirectorRepository directorRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should get list of directors")
    public void shouldGetListOfDirectors() throws IOException {
        var director = directorProvider.createAndSaveRandomDirector();
        var secondDirector = directorProvider.createAndSaveRandomDirector();
        var thirdDirector = directorProvider.createAndSaveRandomDirector();
        var directors = List.of(director, secondDirector, thirdDirector);

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/directors")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var list = (List<Director>) mapper.readValue(response.asByteArray(), new TypeReference<List<Director>>() {
        });

        assertThat(list).hasSize(3);
        assertThat(list).usingFieldByFieldElementComparator().containsAll(directors);
    }

    @Test
    @DisplayName("Should return 401 when try to add new director without login")
    public void shouldReturn401WhenTryToAddNewDirectorWithoutLogin() {
        var director = new Director("name", "surname");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(director)
                .post("/api/directors")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should return 403 when try to add new director as user")
    public void shouldReturn403WhenTryToAddNewDirectorAsUser() {
        var authToken = logInProvider.logInAsUser();
        var director = new Director("name", "surname");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .body(director)
                .post("/api/directors")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should add new director when logged as admin")
    public void shouldAddNewDirectorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var director = new Director("name", "surname");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .body(director)
                .post("/api/directors")
                .then()
                .statusCode(201);

        var directors = directorRepository.findAll();

        assertThat(directors).usingElementComparatorIgnoringFields("id").contains(director);
    }

    @Test
    @DisplayName("Should return 401 when try to delete director without login")
    public void shouldReturn401WhenTryToDeleteDirectorWithoutLogin() {
        var director = directorProvider.createAndSaveRandomDirector();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/directors/" + director.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should return 403 when try to delete director as user")
    public void shouldReturn401WhenTryToDeleteDirectorAsUser() {
        var authToken = logInProvider.logInAsUser();
        var director = directorProvider.createAndSaveRandomDirector();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/directors/" + director.getId())
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should return 404 deleting not existing director when logged as admin")
    public void shouldReturn404WhenDeletingNotExistingDirectorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/directors/" + UUID.randomUUID())
                .then()
                .statusCode(404);

        var directors = directorRepository.findAll();
        assertThat(directors).isEmpty();
    }

    @Test
    @DisplayName("Should delete director when logged as admin")
    public void shouldDeleteDirectorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var director = directorProvider.createAndSaveRandomDirector();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/directors/" + director.getId())
                .then()
                .statusCode(200);

        var directors = directorRepository.findAll();
        assertThat(directors).usingElementComparatorIgnoringFields("id").doesNotContain(director);
    }
}