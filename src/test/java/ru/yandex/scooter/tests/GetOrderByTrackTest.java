package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.Order;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

public class GetOrderByTrackTest {

    private ScooterApiClient scooterApiClient;
    private int courierId;
    private int track;
    private int idOrder;
    private Order order;
    private Courier courier;

    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
    }

    @Test
    @DisplayName("Получение заказа по его номеру")
    public void GetOrderByTrackValid(){
        order = Order.getRandom();

        track = scooterApiClient.createOrders(order)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");
        assertThat("Track некоректный", track, is(not(0)));

        idOrder = scooterApiClient.getOrdersByTrack(track)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("order.id");

        assertThat("Id заказа некоректный", idOrder, is(not(0)));

    }

    @Test
    @DisplayName("Получение заказа с несуществующим заказом")
    public void GetOrderByTrackInvalid(){
        track = 0;
        String message = "Заказ не найден";

        String getOrderError = scooterApiClient.getOrdersByTrack(track)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        assertThat("Сообщение об ошибке некорректно", getOrderError, containsString(message));

    }

    @Test
    @DisplayName("Получение заказа без номера заказа")
    public void GetOrderWithoutTrack(){
        String message = "Недостаточно данных для поиска";

        String getOrderError = scooterApiClient.getOrdersByTrack()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertThat("Сообщение об ошибке некорректно", getOrderError, containsString(message));

    }
}
