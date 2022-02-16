package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.models.Order;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

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
    }

    @Test
    @DisplayName("Получение заказа с несуществующим заказом")
    public void getOrderByTrackInvalid() {
        track = 0;
        String message = "Заказ не найден";

        String getOrderError = ordersApiClient.getOrdersByTrack(track)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        assertTrue("Сообщение об ошибке некорректно", getOrderError.contains(message));

    }

    @Test
    @DisplayName("Получение заказа без номера заказа")
    public void getOrderWithoutTrack() {
        String message = "Недостаточно данных для поиска";

        String getOrderError = ordersApiClient.getOrdersByTrack()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertTrue("Сообщение об ошибке некорректно", getOrderError.contains(message));

    }
}
