package ru.yandex.scooter.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierApiClient extends ScooterApiClient {

    public final String PATH = Endpoints.BASE_URL + Endpoints.COURIER_URL;

    @Step("Courier - Создание курьера {courier}")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Courier - Логин курьера в системе как {courierCredentials}")
    public ValidatableResponse loginCourier(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(PATH + "login/")
                .then();
    }

    @Step("Courier - Удаление курьера {courierId}")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(PATH + courierId)
                .then();
    }

}
