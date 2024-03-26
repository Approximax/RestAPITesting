package tests;

import models.LoginBodyModel;
import models.LoginResponseModel;
import models.UserDataModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.LoginSpecs.loginRequestSpec;
import static specs.LoginSpecs.loginResponseSpec;

@Tag("reqres")
public class ApiTest extends TestBase {

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
        testData.setEmail("eve.holt@reqres.in");
        testData.setPassword("pistol");

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
                        .get("/unknown/23")
                        .then()
                        .statusCode(404)
                        .spec(loginResponseSpec));
    }

    @Test
    @DisplayName("Проверка id пользователя")
    void singleUserId() {
        UserDataModel userDataModel = step("Отправка запроса на получение данных пользователя", () ->
                given(loginRequestSpec)
                        .when()
                        .get("/users/2")
                        .then()
                        .statusCode(200)
                        .spec(loginResponseSpec)
                        .extract().as(UserDataModel.class));

        step("Проверка соответствия id пользователя запрашиваемому", () ->
                assertEquals(2, userDataModel.getData().getId()));
    }
}
