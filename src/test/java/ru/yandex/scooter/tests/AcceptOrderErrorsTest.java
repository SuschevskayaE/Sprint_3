package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.utils.ScooterGenerateOrderData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AcceptOrderErrorsTest {

    private OrdersApiClient ordersApiClient;
    private ScooterGenerateOrderData scooterGenerateOrderData;
    private int courierId;
    private int idOrder;

    @Before
    public void setUp() {
        ordersApiClient = new OrdersApiClient();
        scooterGenerateOrderData = new ScooterGenerateOrderData();
        idOrder = scooterGenerateOrderData.createRandomOrder();
    }

    @Test
    @DisplayName("Принять заказ без id курьера")
    public void acceptOrderWithoutIdCourier() {
        String message = "Недостаточно данных для поиска";

        ValidatableResponse response = ordersApiClient.acceptOrders(idOrder);
        int statusCode = response.extract().statusCode();
        String acceptOrderError = response.extract().path("message");

        assertEquals("statusCode неверный", 400, statusCode);
        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }

    @Test
    @DisplayName("Принять заказ с некорректным id курьера")
    public void acceptOrderInvalidIdCourier() {
        String message = "Курьера с таким id не существует";
        courierId = 0;

        ValidatableResponse response = ordersApiClient.acceptOrders(idOrder, courierId);
        int statusCode = response.extract().statusCode();
        String acceptOrderError = response.extract().path("message");

        assertEquals("statusCode неверный", 404, statusCode);
        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }
}
