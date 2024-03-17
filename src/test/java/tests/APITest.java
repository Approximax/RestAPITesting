package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class APITest {

    private final String base = "https://reqres.in/";

    @Test
    @Tag("reqres")
    @DisplayName("Успешная регистрация с корректными данными")
    void successfulLogin() {

        String test_data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(test_data)
                .when()
                .post(base + "api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @Tag("reqres")
    @DisplayName("Успешная регистрация с минимальными данными")
    void successfulRegistration() {
        String test_credentials =  "{ \"email\": \"java_chemp@apr.in\", \"password\": \"overcome\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(test_credentials)
                .when()
                .post(base + "api/registration")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("email", is("java_chemp@apr.in"),
                        "password", is("overcome"));
    }

    @Test
    @Tag("reqres")
    @DisplayName("Некорректная регистрация с отсутствующим паролем")
    void unsuccessfulRegistration() {
        String test_login = "{\"email\": \"sydney@fife\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(test_login)
                .when()
                .post(base + "api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @Tag("reqres")
    @DisplayName("Пользователь не найден")
    void userNotFound() {
        given()
                .log().uri()
                .when()
                .get(base + "api/users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
}
