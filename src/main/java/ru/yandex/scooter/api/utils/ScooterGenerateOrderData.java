package ru.yandex.scooter.api.utils;

import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.models.Order;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ScooterGenerateOrderData {
    private OrdersApiClient ordersApiClient = new OrdersApiClient();
    private int track;
    private int idOrder;
    private Order order;

    public int createRandomOrder() {
        order = Order.getRandom();

        ValidatableResponse response = ordersApiClient.createOrders(order);
        int statusCode = response.extract().statusCode();
        track = response.extract().path("track");

        assertEquals("statusCode неверный", 201, statusCode);
        assertNotEquals("Track некоректный", 0, track);


        ValidatableResponse responseOrder = ordersApiClient.getOrdersByTrack(track);
        int statusCodeOrder = responseOrder.extract().statusCode();
        idOrder = responseOrder.extract().path("order.id");

        assertEquals("statusCode неверный", 200, statusCodeOrder);
        assertNotEquals("Id заказа некоректный", 0, idOrder);

        return idOrder;
    }

    public boolean finishOrder(int idOrder) {
        ValidatableResponse response = ordersApiClient.finishOrders(idOrder);
        int statusCode = response.extract().statusCode();
        boolean errorMessage = response.extract().path("ok");
        assertEquals("statusCode неверный", 200, statusCode);
        return errorMessage;
    }

    public boolean cancelOrder(int trackOrder) {
        ValidatableResponse response = ordersApiClient.cancelOrders(trackOrder);
        int statusCode = response.extract().statusCode();
        boolean errorMessage = response.extract().path("ok");
        assertEquals("statusCode неверный", 200, statusCode);
        return errorMessage;
    }
}
