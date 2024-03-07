package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APITest {

    @Test
    void singleUserTest () {
        given()
                .when()
                    .get("https://reqres.in/api/users/2")
                .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body(matchesJsonSchemaInClasspath("schemas/single-user-schema.json"));
    }

    @Test
    void userNotFoundTest() {
        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(404);
    }
}
