package com.movie.database.movie_database.movie.rate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.rate.domain.MovieRateRepository;
import com.movie.database.movie_database.utils.LogInProvider;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.MovieRateProvider;
import com.movie.database.movie_database.utils.UserProvider;
import com.movie.database.movie_database.utils.factories.MovieFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;


@MovieDbIntegrationTest
class MovieRateControllerTest {

    @Autowired
    private MovieRateProvider movieRateProvider;
    @Autowired
    private MovieFactory movieFactory;
    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private MovieRateRepository movieRateRepository;
    @Autowired
    private UserProvider userProvider;

    @LocalServerPort
    private int port;


    @Test
    @DisplayName("Should return movie average rate")
    public void shouldReturnMovieAverageRate() throws IOException {
        var rate = 4.0;
        var secondRate = 1.2;
        var thirdRate = 4.2;
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();
        movieRateProvider.createMovieRateAndSave(movie, rate);
        movieRateProvider.createMovieRateAndSave(movie, secondRate);
        movieRateProvider.createMovieRateAndSave(movie, thirdRate);
        var average = Stream.of(rate, secondRate, thirdRate)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        var response = given()
                .port(port)
                .when()
                .param("movieId", movie.getId())
                .get("/api/movie-rate")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var averageFromResponse = mapper.readValue(response.asByteArray(), Double.class);

        assertThat(average).isEqualTo(averageFromResponse);
    }

    @Test
    @DisplayName("Should return zero when movie has no rates")
    public void shouldReturnZeroWhenMovieHasNoRate() throws IOException {
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        var response = given()
                .port(port)
                .when()
                .param("movieId", movie.getId())
                .get("/api/movie-rate")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var averageFromResponse = mapper.readValue(response.asByteArray(), Double.class);

        assertThat(0.0).isEqualTo(averageFromResponse);
    }

    @Test
    @DisplayName("Should return 401 when try to add rate without login")
    public void shouldReturn401WhenTryToAddRateWithoutLogin() {
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        given()
                .port(port)
                .when()
                .param("movieId", movie.getId())
                .param("rate", 1.0)
                .post("/api/movie-rate")
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Should add new movie rate")
    public void shouldAddNewMovieRate() {
        var user = userProvider.createActivatedUserWithUserRole();
        var authToken = logInProvider.logIn(user);
        var rate = 1.0;
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .param("movieId", movie.getId())
                .param("rate", rate)
                .post("/api/movie-rate")
                .then()
                .statusCode(200);

        assertThat(movieRateRepository.findByApplicationUserAndMovie(user, movie)).isPresent();
        assertThat(movieRateRepository.findByApplicationUserAndMovie(user, movie).get().getRate()).isEqualTo(rate);
    }

    @Test
    @DisplayName("Should update movie rate")
    public void shouldUpdateMovieRate() {
        var user = userProvider.createActivatedUserWithUserRole();
        var authToken = logInProvider.logIn(user);
        var rate = 1.0;
        var updatedRate = 5.0;
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        movieRateProvider.createMovieRateAndSave(user, movie, rate);

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .param("movieId", movie.getId())
                .param("rate", updatedRate)
                .post("/api/movie-rate")
                .then()
                .statusCode(200);

        assertThat(movieRateRepository.findByApplicationUserAndMovie(user, movie)).isPresent();
        assertThat(movieRateRepository.findByApplicationUserAndMovie(user, movie).get().getRate()).isEqualTo(updatedRate);
    }

}