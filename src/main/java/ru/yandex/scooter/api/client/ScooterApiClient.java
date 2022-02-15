package ru.yandex.scooter.api.client;

import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.models.Order;
import ru.yandex.scooter.api.models.Track;

public class ScooterApiClient {

    protected RequestSpecification getBaseReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Endpoints.BASE_URL)
                .build();
    }

    @Step("Courier - Создание курьера {courier}")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseReqSpec())
                .body(courier)
                .when()
                .post(Endpoints.BASE_URL + Endpoints.COURIER_URL)
                .then();
    }

    @Step("Courier - Логин курьера в системе как {courierCredentials}")
    public ValidatableResponse loginCourier(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(courierCredentials)
                .when()
                .post(Endpoints.BASE_URL + Endpoints.COURIER_URL +"login/")
                .then();
    }

    @Step("Courier - Удаление курьера {courierId}")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .delete(Endpoints.BASE_URL + Endpoints.COURIER_URL+ courierId)
                .then();
    }

    @Step("Orders - Создание заказа {order}")
    public ValidatableResponse createOrders(Order order) {
        return given()
                .spec(getBaseReqSpec())
                .body(order)
                .when()
                .post(Endpoints.BASE_URL +Endpoints.ORDERS_URL)
                .then();
    }

    @Step("Orders - Отменить заказ {orderTrack}")
    public ValidatableResponse cancelOrders(int orderTrack) {
        return given()
                .spec(getBaseReqSpec())
                .body(new Track(orderTrack))
                .when()
                .put(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/cancel")
                .then();
    }

    @Step("Orders - Завершить заказ {id}")
    public ValidatableResponse finishOrders(int id) {
        String body = "{\"id\":\"" + id + "\"}";
        return given()
                .spec(getBaseReqSpec())
                .body(body)
                .when()
                .put(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/finish/"+ id)
                .then();
    }

    @Step("Orders - Получение списка заказов")
    public Response getListOfOrders() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(Endpoints.BASE_URL + Endpoints.ORDERS_URL);
    }

    @Step("Orders - Принять заказ {orderId}")
    public ValidatableResponse acceptOrders(int orderId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .put(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/accept/" +orderId)
                .then();
    }

    @Step("Orders - Принять заказ {orderId} курьера {courierId}")
    public ValidatableResponse acceptOrders(int orderId, int courierId) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .queryParam("courierId",courierId)
                .put(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/accept/" +orderId)
                .then();
    }


    @Step("Orders - Получить заказ по его номеру {track}")
    public ValidatableResponse getOrdersByTrack(int track) {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .queryParam("t",track)
                .get(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/track")
                .then();
    }

    @Step("Orders - Получить заказ без номера заказа")
    public ValidatableResponse getOrdersByTrack() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/track")
                .then();
    }

}
