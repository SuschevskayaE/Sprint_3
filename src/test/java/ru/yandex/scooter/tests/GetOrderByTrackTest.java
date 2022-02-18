package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.models.Order;

import static org.junit.Assert.*;

public class GetOrderByTrackTest {

    private OrdersApiClient ordersApiClient;
    private int track;
    private int idOrder;
    private Order order;

    @Before
    public void setUp() {
        ordersApiClient = new OrdersApiClient();
    }

    @Test
    @DisplayName("Получение заказа по его номеру")
    public void getOrderByTrackValid() {
        order = Order.getRandom();

        ValidatableResponse response = ordersApiClient.createOrders(order);
        int statusCode = response.extract().statusCode();
        track = response.extract().path("track");

        ValidatableResponse responseOrder = ordersApiClient.getOrdersByTrack(track);
        int statusCodeOrder = responseOrder.extract().statusCode();
        idOrder = responseOrder.extract().path("order.id");

        assertEquals("statusCode неверный", 201, statusCode);
        assertEquals("statusCode неверный", 200, statusCodeOrder);
        assertNotEquals("Track некоректный", 0, track);
        assertNotEquals("Id заказа некоректный", 0, idOrder);
    }

    @Test
    @DisplayName("Получение заказа с несуществующим заказом")
    public void getOrderByTrackInvalid() {
        track = 0;
        String message = "Заказ не найден";

        ValidatableResponse response = ordersApiClient.getOrdersByTrack(track);
        int statusCode = response.extract().statusCode();
        String getOrderError = response.extract().path("message");

        assertEquals("statusCode неверный", 404, statusCode);
        assertTrue("Сообщение об ошибке некорректно", getOrderError.contains(message));

    }

    @Test
    @DisplayName("Получение заказа без номера заказа")
    public void getOrderWithoutTrack() {
        String message = "Недостаточно данных для поиска";

        ValidatableResponse response = ordersApiClient.getOrdersByTrack();
        int statusCode = response.extract().statusCode();
        String getOrderError = response.extract().path("message");

        assertEquals("statusCode неверный", 400, statusCode);
        assertTrue("Сообщение об ошибке некорректно", getOrderError.contains(message));

    }
}
