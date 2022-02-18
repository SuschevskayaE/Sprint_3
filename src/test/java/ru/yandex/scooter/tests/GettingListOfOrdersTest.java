package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.models.OrderResponse;
import ru.yandex.scooter.api.models.Orders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GettingListOfOrdersTest {
    private OrdersApiClient ordersApiClient;
    private Response response;


    @Before
    public void setUp() {
        ordersApiClient = new OrdersApiClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getListOfOrdersWithoutParams() {

        response = ordersApiClient.getListOfOrders();
        int statusCode = response.then().extract().statusCode();
        OrderResponse[] orderResponse = response.body().as(Orders.class).getOrders();

        assertEquals("statusCode неверный", 200, statusCode);
        assertTrue("Нет заказов", orderResponse.length > 0);
    }
}
