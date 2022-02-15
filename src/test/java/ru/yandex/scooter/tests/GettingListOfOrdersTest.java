package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.models.OrderResponse;
import ru.yandex.scooter.api.models.Orders;

import static org.junit.Assert.assertTrue;

public class GettingListOfOrdersTest {
    private ScooterApiClient scooterApiClient;
    private Response response;


    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    public void GetListOfOrdersWithoutParams(){

        response = scooterApiClient.getListOfOrders();
        response.then().assertThat().statusCode(200);
        OrderResponse[] orderResponse = response.body().as(Orders.class).getOrders();
        assertTrue("Нет заказов",orderResponse.length > 0);
    }


}
