package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class APITest {

    private static String base = "https://reqres.in/";

    @Test
    @Tag("reqres")
    void singleUserTest () {
        given()
                .when()
                    .get(base + "api/users/2")
                .then()
                    .log().status()
                    .log().body()
                    .statusCode(200);
    }

    @Test
    @Tag("reqres")
    @DisplayName("Проверка кода ответа, при POST создания пользователя")
    void checkStatusCodeCreate() {
        given()
                .log().uri()
                .when()
                    .post(base + "/api/users")
                .then()
                    .log().status()
                    .log().body()
                    .statusCode(201);
    }

    @Test
    void validateResponse() {
        String test_data = "{\"name\": \"morpheus\", \"job\": \"zion resident\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(test_data)
        .when()
                .put(base + "api/users/2")
        .then()
                .log().status()
                .log().status()
                .statusCode(201)
                .body("name", is(""))


    }
}
