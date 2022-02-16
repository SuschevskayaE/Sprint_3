package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.OrdersApiClient;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.utils.ScooterGenerateCurierData;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

public class AcceptOrderCurierErrorsTest {
    private OrdersApiClient ordersApiClient;
    private ScooterGenerateCurierData scooterGenerateCurierData;
    private int courierId;
    private int idOrder;

    @Before
    public void setUp() {
        ordersApiClient = new OrdersApiClient();
        scooterGenerateCurierData = new ScooterGenerateCurierData();
        courierId = scooterGenerateCurierData.createRandomCourier();
    }

    @After
    public void tearDown() {
        boolean isCourierDeleted = scooterGenerateCurierData.deleteCourier(courierId);
        assertTrue("Курьер не удален", isCourierDeleted);
    }

    @Test
    @DisplayName("Принять заказ c неверным номером заказа")
    public void acceptOrderInvalidIdOrder() {
        idOrder = 0;
        String message = "Заказа с таким id не существует";

        String acceptOrderError = ordersApiClient.acceptOrders(idOrder, courierId)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }

    @Test
    @DisplayName("Принять заказ без номера заказа")
    public void acceptOrderWithoutIdOrder() {
        String message = "Недостаточно данных для поиска";

        String acceptOrderError = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId", courierId)
                .put(Endpoints.BASE_URL + Endpoints.ORDERS_URL + "/accept/")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertTrue("Сообщение об ошибке некорректно", acceptOrderError.contains(message));
    }
}
