package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.utils.ScooterGenerateData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcceptOrderTest {

    private ScooterApiClient scooterApiClient;
    private ScooterGenerateData scooterGenerateData;
    private int courierId;
    private int idOrder;
    private Courier courier;

    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
        scooterGenerateData = new ScooterGenerateData();
    }

    @Test
    @DisplayName("Принять заказ")
    public void acceptOrderValid(){

        idOrder = scooterGenerateData.createRandomOrder();
        courierId = scooterGenerateData.createRandomCourier();

        boolean isAcceptOrder = scooterApiClient.acceptOrders(idOrder, courierId)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("ok");

        boolean isCourierDeleted = scooterGenerateData.deleteCourier(courierId);
        assertTrue(isCourierDeleted, "Курьер не удален");
        boolean isOrderFinished = scooterGenerateData.finishOrder(idOrder);
        assertTrue(isOrderFinished, "Орден не закрыт");

        assertTrue(isAcceptOrder, "Заказ не принят");
    }

    @Test
    @DisplayName("Принять заказ без id курьера")
    public void acceptOrderWithoutIdCourier(){
        String message = "Недостаточно данных для поиска";

        idOrder = scooterGenerateData.createRandomOrder();

        String acceptOrderError = scooterApiClient.acceptOrders(idOrder)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");
        assertThat("Сообщение об ошибке некорректно", acceptOrderError, containsString(message));
    }

    @Test
    @DisplayName("Принять заказ с некорректным id курьера")
    public void acceptOrderInvalidIdCourier(){
        String message = "Курьера с таким id не существует";
        courierId = 0;

        idOrder = scooterGenerateData.createRandomOrder();

        String acceptOrderError = scooterApiClient.acceptOrders(idOrder, courierId)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");
        assertThat("Сообщение об ошибке некорректно", acceptOrderError, containsString(message));
    }

    @Test
    @DisplayName("Принять заказ c неверным номером заказа")
    public void acceptOrderInvalidIdOrder(){
        idOrder = 0;
        String message = "Заказа с таким id не существует";

        courierId = scooterGenerateData.createRandomCourier();

        String acceptOrderError = scooterApiClient.acceptOrders(idOrder, courierId)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("ok");

        boolean isCourierDeleted = scooterGenerateData.deleteCourier(courierId);
        assertTrue(isCourierDeleted, "Курьер не удален");

        assertThat("Сообщение об ошибке некорректно", acceptOrderError, containsString(message));
    }

    @Test
    @DisplayName("Принять заказ без номера заказа")
    public void acceptOrderWithoutIdOrder(){
        String message = "Недостаточно данных для поиска";

        courierId = scooterGenerateData.createRandomCourier();

        assertThat("Id курьера некоректный", courierId, is(not(0)));

        String acceptOrderError = given()
                .header("Content-type", "application/json")
                .when()
                .queryParam("courierId",courierId)
                .put(Endpoints.BASE_URL +Endpoints.ORDERS_URL +"/accept/")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .path("ok");

        boolean isCourierDeleted = scooterGenerateData.deleteCourier(courierId);
        assertTrue(isCourierDeleted, "Курьер не удален");

        assertThat("Сообщение об ошибке некорректно", acceptOrderError, containsString(message));
    }

}
