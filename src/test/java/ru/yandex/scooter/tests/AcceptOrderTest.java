package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.utils.ScooterGenerateCurierData;
import ru.yandex.scooter.api.utils.ScooterGenerateOrderData;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AcceptOrderTest {

    private OrdersApiClient ordersApiClient;
    private ScooterGenerateOrderData scooterGenerateOrderData;
    private ScooterGenerateCurierData scooterGenerateCurierData;
    private int courierId;
    private int idOrder;

    @Before
    public void setUp() {
        ordersApiClient = new OrdersApiClient();
        scooterGenerateOrderData = new ScooterGenerateOrderData();
        scooterGenerateCurierData = new ScooterGenerateCurierData();

        idOrder = scooterGenerateOrderData.createRandomOrder();
        courierId = scooterGenerateCurierData.createRandomCourier();
    }

    @After
    public void tearDown() {
        boolean isCourierDeleted = scooterGenerateCurierData.deleteCourier(courierId);
        assertTrue("Курьер не удален", isCourierDeleted);
        boolean isOrderFinished = scooterGenerateOrderData.finishOrder(idOrder);
        assertTrue("Орден не закрыт", isOrderFinished);
    }

    @Test
    @DisplayName("Принять заказ")
    public void acceptOrderValid() {

        ValidatableResponse response = ordersApiClient.acceptOrders(idOrder, courierId);
        int statusCode = response.extract().statusCode();
        boolean isAcceptOrder = response.extract().path("ok");

        assertEquals("statusCode неверный", 200, statusCode);
        assertTrue("Заказ не принят", isAcceptOrder);
    }
}
