package com.movie.database.movie_database.movie.category;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.movie.category.domain.Category;
import com.movie.database.movie_database.movie.category.domain.CategoryRepository;
import com.movie.database.movie_database.utils.CategoryProvider;
import com.movie.database.movie_database.utils.LogInProvider;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
class CategoryControllerTest {

    @Autowired
    private CategoryProvider categoryProvider;
    @Autowired
    private LogInProvider logInProvider;
    @Autowired
    private CategoryRepository categoryRepository;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should return list of all categories")
    public void shouldReturnListOfAllCategories() throws IOException {
        var category = categoryProvider.createCategory("category");
        var category1 = categoryProvider.createCategory("category2");

        var response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/categories")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var list = (List<Category>) mapper.readValue(response.asByteArray(), new TypeReference<List<Category>>() {
        });

        assertThat(list).usingFieldByFieldElementComparator().containsAll(List.of(category, category1));
    }

    @Test
    @DisplayName("Should add new category when logged in as admin")
    public void shouldAddNewCategoryWhenLoggedInAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var categoryName = "category";
        var category = new Category(categoryName);

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", authToken)
                .body(category)
                .post("/api/categories")
                .then()
                .statusCode(201);

        var categories = categoryRepository.findAll().stream().map(Category::getCategory).collect(Collectors.toList());
        assertThat(categories).contains(categoryName);
    }

    @Test
    @DisplayName("Should not add new category when logged in as user")
    public void shouldNotAddNewCategoryWhenLoggedInAsUser() {
        var authToken = logInProvider.logInAsUser();
        var categoryName = "category";
        var category = new Category(categoryName);

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", authToken)
                .body(category)
                .post("/api/categories")
                .then()
                .statusCode(403);

        var categories = categoryRepository.findAll().stream().map(Category::getCategory).collect(Collectors.toList());
        assertThat(categories).doesNotContain(categoryName);
    }

    @Test
    @DisplayName("Should not add new category when not logged in")
    public void shouldNotAddNewCategoryWhenNotLoggedIn() {
        var categoryName = "category";
        var category = new Category(categoryName);

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .body(category)
                .post("/api/categories")
                .then()
                .statusCode(401);

        var categories = categoryRepository.findAll().stream().map(Category::getCategory).collect(Collectors.toList());
        assertThat(categories).doesNotContain(categoryName);
    }

    @Test
    @DisplayName("Should delete category when logged in as admin")
    public void shouldDeleteCategoryWhenLoggedInAsAdmin() {
        var authToken = logInProvider.logInAsAdmin();
        var categoryName = "category";
        var category = categoryRepository.save(new Category(categoryName));

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", authToken)
                .delete("/api/categories/" + category.getId())
                .then()
                .statusCode(200);

        var categories = categoryRepository.findAll().stream().map(Category::getCategory).collect(Collectors.toList());
        assertThat(categories).doesNotContain(categoryName);
    }

    @Test
    @DisplayName("Should not delete category when logged in as user")
    public void shouldNotDeleteCategoryWhenLoggedInAsUser() {
        var authToken = logInProvider.logInAsUser();
        var categoryName = "category";
        var category = categoryRepository.save(new Category(categoryName));

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .header("Authorization", authToken)
                .delete("/api/categories/" + category.getId())
                .then()
                .statusCode(403);

        var categories = categoryRepository.findAll().stream().map(Category::getCategory).collect(Collectors.toList());
        assertThat(categories).contains(categoryName);
    }


    @Test
    @DisplayName("Should not delete category when not logged in")
    public void shouldNotDeleteCategoryWhenNotLoggedIn() {
        var categoryName = "category";
        var category = categoryRepository.save(new Category(categoryName));

        given()
                .port(port)
                .when()
                .contentType(ContentType.JSON)
                .body(category)
                .delete("/api/categories/" + category.getId())
                .then()
                .statusCode(401);

        var categories = categoryRepository.findAll().stream().map(Category::getCategory).collect(Collectors.toList());
        assertThat(categories).contains(categoryName);
    }
}