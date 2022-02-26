package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.utils.ScooterGenerateCurierData;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AcceptOrderCurierErrorsTest {
    private OrdersApiClient ordersApiClient;
    private ScooterGenerateCurierData scooterGenerateCurierData;
    private int courierId;
    private int idOrder;

    @Before
    public void setUp() {
        ordersApiClient = new OrdersApiClient();
        scooterGenerateCurierData = new ScooterGenerateCurierData();
        courierId = scooterGenerateCurierData.createRandomCourier();
    }

    @After
    public void tearDown() {
        boolean isCourierDeleted = scooterGenerateCurierData.deleteCourier(courierId);
        assertTrue("Курьер не удален", isCourierDeleted);
    }

    @Test
    @DisplayName("Принять заказ c неверным номером заказа")
    public void acceptOrderInvalidIdOrder() {
        idOrder = 0;
        String message = "Заказа с таким id не существует";

        ValidatableResponse response = ordersApiClient.acceptOrders(idOrder, courierId);
        int statusCode = response.extract().statusCode();
        String acceptOrderError = response.extract().path("message");

        assertEquals("statusCode неверный", 404, statusCode);
        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }

    @Test
    @DisplayName("Принять заказ без номера заказа")
    public void acceptOrderWithoutIdOrder() {
        String message = "Недостаточно данных для поиска";

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                .put(Endpoints.BASE_URL + Endpoints.ORDERS_URL + "/accept/")
                .then();

        int statusCode = response.extract().statusCode();

        String acceptOrderError = response.extract().path("message");

        assertEquals("statusCode неверный", 400, statusCode);
        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }
}
