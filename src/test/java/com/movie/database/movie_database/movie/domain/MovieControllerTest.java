package com.movie.database.movie_database.movie.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.factories.MovieFactory;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
class MovieControllerTest {

    @Autowired
    private MovieFactory movieFactory;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should get list of movies")
    public void shouldGetListOfMovies() throws IOException {

        var firstMovie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        var secondMovie = movieFactory
                .withTitle("secondMovieName")
                .withDescription("secondDescription")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/movies")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var movies = (List<Movie>) mapper.readValue(response.asByteArray(), new TypeReference<List<Movie>>() {
        });

        assertThat(movies).hasSize(2);
        assertThat(movies.stream().anyMatch(movie -> movie.getId().equals(firstMovie.getId()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getTitle().equals(firstMovie.getTitle()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getDescription().equals(firstMovie.getDescription()))).isTrue();

        assertThat(movies.stream().anyMatch(movie -> movie.getId().equals(secondMovie.getId()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getTitle().equals(secondMovie.getTitle()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getDescription().equals(secondMovie.getDescription()))).isTrue();
        assertThat(movies.get(0).getCategories()).hasSize(1);
        assertThat(movies.get(1).getCategories()).hasSize(1);
        assertThat(movies.get(0).getCategories().get(0).getCategory()).isEqualTo("Category");
        assertThat(movies.get(1).getCategories().get(0).getCategory()).isEqualTo("Category");
    }
}