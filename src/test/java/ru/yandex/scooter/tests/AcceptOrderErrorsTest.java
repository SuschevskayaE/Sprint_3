package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.utils.ScooterGenerateOrderData;

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

        String acceptOrderError = ordersApiClient.acceptOrders(idOrder)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");
        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }

    @Test
    @DisplayName("Принять заказ с некорректным id курьера")
    public void acceptOrderInvalidIdCourier() {
        String message = "Курьера с таким id не существует";
        courierId = 0;

        String acceptOrderError = ordersApiClient.acceptOrders(idOrder, courierId)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");
        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }
}
