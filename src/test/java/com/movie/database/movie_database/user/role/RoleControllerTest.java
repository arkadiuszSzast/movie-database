package com.movie.database.movie_database.user.role;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.database.movie_database.user.role.domain.Role;
import com.movie.database.movie_database.utils.LogInProvider;
import com.movie.database.movie_database.utils.MovieDbIntegrationTest;
import com.movie.database.movie_database.utils.RolesProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@MovieDbIntegrationTest
class RoleControllerTest {

    @Autowired
    private RolesProvider rolesProvider;
    @Autowired
    private LogInProvider logInProvider;

    @LocalServerPort
    private int port;


    @Test
    @DisplayName("Should return list of all roles when logged as admin")
    public void shouldReturnListOfAllRolesWhenLoggedInAsAdmin() throws IOException {
        var authToken = logInProvider.logInAsAdmin();
        var userRole = rolesProvider.createUserRole();
        var adminRole = rolesProvider.createAdminRole();

        var response = given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .get("/api/roles")
                .then()
                .statusCode(200)
                .extract()
                .response();

        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var list = (List<Role>) mapper.readValue(response.asByteArray(), new TypeReference<List<Role>>() {
        });

        assertThat(list).usingFieldByFieldElementComparator().contains(userRole, adminRole);
    }

    @Test
    @DisplayName("Should return 403 when getting roles as user")
    public void shouldReturn403WhenGettingRolesAsUser() {
        var authToken = logInProvider.logInAsUser();

        given()
                .port(port)
                .when()
                .header("Authorization", authToken)
                .get("/api/roles")
                .then()
                .statusCode(403)
                .extract()
                .response();
    }

    @Test
    @DisplayName("Should return 401 when getting roles without log in")
    public void shouldReturn403WhenGettingRolesWithoutLogIn() {

        given()
                .port(port)
                .when()
                .get("/api/roles")
                .then()
                .statusCode(401)
                .extract()
                .response();
    }
}