package ru.yandex.scooter.api.utils;

import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.models.Order;
import static org.junit.Assert.assertNotEquals;

public class ScooterGenerateOrderData {
    private OrdersApiClient ordersApiClient = new OrdersApiClient();
    private int track;
    private int idOrder;
    private Order order;

    public int createRandomOrder() {
        order = Order.getRandom();

        track = ordersApiClient.createOrders(order)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");

        idOrder = ordersApiClient.getOrdersByTrack(track)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("order.id");

        assertNotEquals("Track некоректный", 0, track);
        assertNotEquals("Id заказа некоректный", 0, idOrder);

        return idOrder;
    }

    public boolean finishOrder(int idOrder) {
        return ordersApiClient.finishOrders(idOrder).assertThat().statusCode(200).extract().path("ok");
    }

    public boolean cancelOrder(int trackOrder) {
        return ordersApiClient.cancelOrders(trackOrder).assertThat().statusCode(200).extract().path("ok");
    }
}
