package tests;

import helpers.TestBase;
import models.LoginBodyModel;
import models.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.LoginSpecs.*;

@Tag("reqres")
public class APITest extends TestBase {

    @Test
    @DisplayName("Успешная авторизация")
    void successfulLogin() {
        LoginBodyModel testData = new LoginBodyModel();
        testData.setEmail("eve.holt@reqres.in");
        testData.setPassword("cityslicka");

        LoginResponseModel response = step("Отправка запроса на авторизацию", () ->
                given(loginRequestSpec)
                        .body(testData)
                .when()
                        .post("/login")
                .then()
                        .statusCode(200)
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));
        step("Проверка наличия токена", () ->
                assertNotNull(response.getToken()));
    }

    @Test
    @DisplayName("Успешная регистрация")
    void successfulRegistration() {
        LoginBodyModel testData = new LoginBodyModel();
        testData.setEmail("java_chemp@apr.in");
        testData.setPassword("overcome");

        LoginResponseModel responseModel = step("Отправка данных на регистрацию пользователя", () ->
                given(loginRequestSpec)
                        .body(testData)
                        .when()
                        .post("/register")
                        .then()
                        .statusCode(200)
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Проверка наличия токена", () ->
                assertNotNull(responseModel.getToken()));
    }

    @Test
    @DisplayName("Регистрация без пароля")
    void unsuccessfulRegistration() {
        LoginBodyModel testData = new LoginBodyModel();
        testData.setEmail("sydney@fife");

        LoginResponseModel responseModel = step("Регистрация пользователя", () ->
                given(loginRequestSpec)
                        .body(testData)
                        .when()
                        .post("/register")
                        .then()
                        .statusCode(400)
                        .spec(loginResponseSpec)
                        .extract().as(LoginResponseModel.class));

        step("Проверка, что в отете пришла ошибка", () ->
                assertEquals("Missing password", responseModel.getError()));
    }

    @Test
    @DisplayName("Пользователь не найден")
    void userNotFound() {
        step("Отправка битого запроса с проверкой статуса ответа", () ->
                given(loginRequestSpec)
                        .when()
                        .post("/unknown/23")
                        .then()
                        .statusCode(404)
                        .spec(loginResponseSpec));
    }
}
