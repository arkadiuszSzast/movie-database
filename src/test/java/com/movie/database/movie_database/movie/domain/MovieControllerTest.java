package com.movie.database.movie_database.movie.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.model.MovieRest;
import com.movie.database.movie_database.utils.*;
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
    @Autowired
    private ActorProvider actorProvider;
    @Autowired
    private DirectorProvider directorProvider;
    @Autowired
    private CategoryProvider categoryProvider;
    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private MovieRepository movieRepository;

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

    @Test
    @DisplayName("Should get list of movies filtered by title")
    public void shouldGetListOfMoviesFilteredByTitle() throws IOException {

        movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        var secondMovie = movieFactory
                .withTitle("secondMovieName")
                .withDescription("secondDescription")
                .withCategories(List.of(new Category("Category2")))
                .createWithSave();

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .param("title", "second")
                .get("/api/movies")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var movies = (List<Movie>) mapper.readValue(response.asByteArray(), new TypeReference<List<Movie>>() {
        });

        assertThat(movies).hasSize(1);
        assertThat(movies.stream().anyMatch(movie -> movie.getId().equals(secondMovie.getId()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getTitle().equals(secondMovie.getTitle()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getDescription().equals(secondMovie.getDescription()))).isTrue();
        assertThat(movies.get(0).getCategories()).hasSize(1);
        assertThat(movies.get(0).getCategories().get(0).getCategory()).isEqualTo("Category2");
    }

    @Test
    @DisplayName("Should get list of movies filtered by actor and category")
    public void shouldGetListOfMoviesFilteredByActorAndCategory() throws IOException {

        var actor = actorProvider.createAndSaveRandomActor();
        var category = categoryProvider.createCategory("category2");

        movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        var secondMovie = movieFactory
                .withTitle("secondMovieName")
                .withDescription("secondDescription")
                .withCategories(List.of(category))
                .withActors(List.of(actor))
                .createWithSave();

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .param("actors", actor.getId())
                .param("categories", category.getId())
                .get("/api/movies")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var movies = (List<Movie>) mapper.readValue(response.asByteArray(), new TypeReference<List<Movie>>() {
        });

        assertThat(movies).hasSize(1);
        assertThat(movies.stream().anyMatch(movie -> movie.getId().equals(secondMovie.getId()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getTitle().equals(secondMovie.getTitle()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getDescription().equals(secondMovie.getDescription()))).isTrue();
        assertThat(movies.get(0).getActors()).usingFieldByFieldElementComparator().containsExactly(actor);
        assertThat(movies.get(0).getCategories()).hasSize(1);
        assertThat(movies.get(0).getCategories().get(0).getCategory()).isEqualTo(category.getCategory());
    }

    @Test
    @DisplayName("Should get list of movies filtered by actor and director")
    public void shouldGetListOfMoviesFilteredByActorAndDirector() throws IOException {

        var actor = actorProvider.createAndSaveRandomActor();
        var director = directorProvider.createAndSaveRandomDirector();
        var category = categoryProvider.createCategory("category2");

        movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .withDirectors(List.of(director))
                .createWithSave();

        var secondMovie = movieFactory
                .withTitle("secondMovieName")
                .withDescription("secondDescription")
                .withCategories(List.of(category))
                .withDirectors(List.of(director))
                .withActors(List.of(actor))
                .createWithSave();

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .param("actors", actor.getId())
                .param("categories", category.getId())
                .get("/api/movies")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var movies = (List<Movie>) mapper.readValue(response.asByteArray(), new TypeReference<List<Movie>>() {
        });

        assertThat(movies).hasSize(1);
        assertThat(movies.stream().anyMatch(movie -> movie.getId().equals(secondMovie.getId()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getTitle().equals(secondMovie.getTitle()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getDescription().equals(secondMovie.getDescription()))).isTrue();
        assertThat(movies.get(0).getActors()).usingFieldByFieldElementComparator().containsExactly(actor);
        assertThat(movies.get(0).getDirectors()).usingFieldByFieldElementComparator().containsExactly(director);
        assertThat(movies.get(0).getCategories()).hasSize(1);
        assertThat(movies.get(0).getCategories().get(0).getCategory()).isEqualTo(category.getCategory());
    }

    @Test
    @DisplayName("Should get list of movies filtered by actor, director, title, category and description")
    public void shouldGetListOfMoviesFilteredByActorAndDirectorAndCategoryAndTitleAndDescription() throws IOException {

        var actor = actorProvider.createAndSaveRandomActor();
        var director = directorProvider.createAndSaveRandomDirector();
        var category = categoryProvider.createCategory("category2");

        movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .withDirectors(List.of(director))
                .createWithSave();

        var secondMovie = movieFactory
                .withTitle("secondMovieName")
                .withDescription("description")
                .withCategories(List.of(category))
                .withDirectors(List.of(director))
                .withActors(List.of(actor))
                .createWithSave();

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .param("actors", actor.getId())
                .param("categories", category.getId())
                .param("directors", director.getId())
                .param("title", secondMovie.getTitle())
                .param("description", secondMovie.getDescription())
                .get("/api/movies")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        var movies = (List<Movie>) mapper.readValue(response.asByteArray(), new TypeReference<List<Movie>>() {
        });

        assertThat(movies).hasSize(1);
        assertThat(movies.stream().anyMatch(movie -> movie.getId().equals(secondMovie.getId()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getTitle().equals(secondMovie.getTitle()))).isTrue();
        assertThat(movies.stream().anyMatch(movie -> movie.getDescription().equals(secondMovie.getDescription()))).isTrue();
        assertThat(movies.get(0).getActors()).usingFieldByFieldElementComparator().containsExactly(actor);
        assertThat(movies.get(0).getDirectors()).usingFieldByFieldElementComparator().containsExactly(director);
        assertThat(movies.get(0).getCategories()).usingFieldByFieldElementComparator().containsExactly(category);
        assertThat(movies.get(0).getCategories()).hasSize(1);
        assertThat(movies.get(0).getCategories().get(0).getCategory()).isEqualTo(category.getCategory());
    }

    @Test
    @DisplayName("Should delete movie by given id")
    public void shouldDeleteMovieByGivenId() {
        var authToken = logInProvider.logInAsAdmin();
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .pathParam("movieId", movie.getId())
                .delete("/api/movies/{movieId}")
                .then()
                .statusCode(200);

        var movieAfterDelete = movieRepository.findById(movie.getId());
        assertThat(movieAfterDelete).isEmpty();
    }

    @Test
    @DisplayName("Should not delete movie by given id when logged as user")
    public void shouldNotDeleteMovieByGivenIdWhenLoggedAsUser() {
        var authToken = logInProvider.logInAsUser();
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .pathParam("movieId", movie.getId())
                .delete("/api/movies/{movieId}")
                .then()
                .statusCode(403);

        var movieAfterDelete = movieRepository.findById(movie.getId());
        assertThat(movieAfterDelete).isPresent();
    }

    @Test
    @DisplayName("Should not delete movie by given id when not logged in")
    public void shouldNotDeleteMovieByGivenIdWhenNotLoggedIn() {
        var movie = movieFactory
                .withTitle("movieName")
                .withDescription("description")
                .withCategories(List.of(new Category("Category")))
                .createWithSave();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .pathParam("movieId", movie.getId())
                .delete("/api/movies/{movieId}")
                .then()
                .statusCode(401);

        var movieAfterDelete = movieRepository.findById(movie.getId());
        assertThat(movieAfterDelete).isPresent();
    }

    @Test
    @DisplayName("Should add new movie")
    public void shouldAddNewMovie() {
        var authToken = logInProvider.logInAsAdmin();
        var movieRest = new MovieRest("title", "description", List.of(), List.of(), List.of());

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .body(movieRest)
                .post("/api/movies")
                .then()
                .statusCode(201);

        var movies = movieRepository.findAll();
        assertThat(movies).hasSize(1);
    }

    @Test
    @DisplayName("Should not add new movie when logged as user")
    public void shouldNotAddNewMovieWhenLoggedAsUser() {
        var authToken = logInProvider.logInAsUser();
        var movieRest = new MovieRest("title", "description", List.of(), List.of(), List.of());

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .header("Authorization", authToken)
                .body(movieRest)
                .post("/api/movies")
                .then()
                .statusCode(403);

        var movies = movieRepository.findAll();
        assertThat(movies).hasSize(0);
    }

    @Test
    @DisplayName("Should not add new movie when not logged in")
    public void shouldNotAddNewMovieWhenNotLoggedIn() {
        var movieRest = new MovieRest("title", "description", List.of(), List.of(), List.of());

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .body(movieRest)
                .post("/api/movies")
                .then()
                .statusCode(401);

        var movies = movieRepository.findAll();
        assertThat(movies).hasSize(0);
    }
}