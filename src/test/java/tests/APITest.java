package tests;

import helpers.TestBase;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.LoginSpecs.loginRequestSpec;
import static specs.LoginSpecs.loginResponseSpec;

@Tag("reqres")
public class APITest extends TestBase {

    @Test
    @DisplayName("Успешная регистрация с корректными данными")
    void successfulLogin() {
        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = step("Отправка запроса", () ->
                given(loginRequestSpec)
                        .body(authData)
                .when()
                        .post(basePath + "login")
                .then()
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));
        step("Проверка ответа", () ->
                assertNotNull(response.getToken()));
    }

    @Test
    @DisplayName("Успешная регистрация с минимальными данными")
    void successfulRegistration() {
        String test_credentials =  "{ \"email\": \"java_chemp@apr.in\", \"password\": \"overcome\" }";

        given()
                .log().uri()
                .contentType(JSON)
                .body(test_credentials)
                .when()
                .post(basePath + "/registration")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("email", is("java_chemp@apr.in"),
                        "password", is("overcome"));
    }

    @Test
    @DisplayName("Некорректная регистрация с отсутствующим паролем")
    void unsuccessfulRegistration() {
        String test_login = "{\"email\": \"sydney@fife\"}";

        given()
                .log().uri()
                .contentType(JSON)
                .body(test_login)
                .when()
                .post(basePath + "/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Пользователь не найден")
    void userNotFound() {
        given()
                .log().uri()
                .when()
                .get(basePath + "users/23")
                .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }
}
