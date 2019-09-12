package com.movie.database.movie_database.actor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.actor.domain.Actor;
import com.movie.database.movie_database.actor.domain.ActorRepository;
import com.movie.database.movie_database.utils.ActorProvider;
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
class ActorControllerTest {

    @Autowired
    private ActorProvider actorProvider;
    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private ActorRepository actorRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should return list of actors")
    public void shouldReturnListOfActors() throws IOException {
        var actor = actorProvider.createAndSaveRandomActor();
        var secondActor = actorProvider.createAndSaveRandomActor();
        var thirdActor = actorProvider.createAndSaveRandomActor();
        var actors = List.of(actor, secondActor, thirdActor);

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/actors")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var list = (List<Actor>) mapper.readValue(response.asByteArray(), new TypeReference<List<Actor>>() {
        });

        assertThat(list).hasSize(3);
        assertThat(list).usingFieldByFieldElementComparator().containsAll(actors);
    }

    @Test
    @DisplayName("Should return 401 when try to add new actor without login")
    public void shouldReturn401WhenTryToAddNewActorWithoutLogin() {
        var actor = new Actor("name", "surname");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(actor)
                .post("/api/actors")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should return 403 when try to add new actor when logged as user")
    public void shouldReturn403WhenTryToAddNewActorWhenLoggedAsUser() {
        var authToken = logInProvider.logInAsUser();
        var actor = new Actor("name", "surname");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .body(actor)
                .post("/api/actors")
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should add new actor when logged as admin")
    public void shouldAddNewDirectorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var actor = new Actor("name", "surname");

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .body(actor)
                .post("/api/actors")
                .then()
                .statusCode(201);

        var directors = actorRepository.findAll();

        assertThat(directors).usingElementComparatorIgnoringFields("id").contains(actor);
    }

    @Test
    @DisplayName("Should return 401 when try to delete actor without login")
    public void shouldReturn401WhenTryToDeleteActorWithoutLogin() {
        var actor = actorProvider.createAndSaveRandomActor();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/actors/" + actor.getId())
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should return 403 when try to delete actor as user")
    public void shouldReturn401WhenTryToDeleteActorAsUser() {
        var authToken = logInProvider.logInAsUser();
        var actor = actorProvider.createAndSaveRandomActor();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/actors/" + actor.getId())
                .then()
                .statusCode(403);
    }

    @Test
    @DisplayName("Should return 404 deleting not existing actor when logged as admin")
    public void shouldReturn404WhenDeletingNotExistingActorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/actors/" + UUID.randomUUID())
                .then()
                .statusCode(404);

        var actors = actorRepository.findAll();
        assertThat(actors).isEmpty();
    }

    @Test
    @DisplayName("Should delete actor when logged as admin")
    public void shouldDeleteDirectorWhenLoggedAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var actor = actorProvider.createAndSaveRandomActor();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .delete("/api/actors/" + actor.getId())
                .then()
                .statusCode(200);

        var actors = actorRepository.findAll();
        assertThat(actors).usingElementComparatorIgnoringFields("id").doesNotContain(actor);
    }
}