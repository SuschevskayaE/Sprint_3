package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.utils.ScooterGenerateCurierData;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteCourierTest {
    private CourierApiClient courierApiClient;
    private ScooterGenerateCurierData scooterGenerateCurierData;
    private int courierId;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        scooterGenerateCurierData = new ScooterGenerateCurierData();
    }

    @Test
    @DisplayName("Удаление курьера")
    public void deleteCourierValid() {

        courierId = scooterGenerateCurierData.createRandomCourier();
        boolean isCourierDeleted = courierApiClient.deleteCourier(courierId).assertThat().statusCode(200).extract().path("ok");
        assertTrue("Курьер не удален", isCourierDeleted);
    }

    @Test
    @DisplayName("Удаление курьера с несуществующим id")
    public void deleteCourierWithInvalidId() {
        courierId = 0;
        String message = "Курьера с таким id нет";

        ValidatableResponse response = courierApiClient.deleteCourier(courierId);
        int statusCode = response.extract().statusCode();
        String courierDeleted = response.extract().path("message");

        assertEquals("statusCode неверный", 404, statusCode);
        assertTrue("Сообщение о удалении курьера некорректно", courierDeleted.contains(message));
    }

    @Test
    @DisplayName("Удаление курьера без id")
    public void deleteCourierWithoutId() {
        String message = "Недостаточно данных для удаления курьера";

        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .when()
                .delete(Endpoints.BASE_URL + Endpoints.COURIER_URL)
                .then();
        int statusCode = response.extract().statusCode();
        String courierDeleted = response.extract().path("message");

        assertEquals("statusCode неверный", 404, statusCode);
        assertTrue("Сообщение о недостаточности данных некорректно", courierDeleted.contains(message));
    }
}
