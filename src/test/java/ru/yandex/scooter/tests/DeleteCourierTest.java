package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.constants.Endpoints;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.utils.ScooterGenerateData;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteCourierTest {
    private ScooterApiClient scooterApiClient;
    private ScooterGenerateData scooterGenerateData;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
        scooterGenerateData = new ScooterGenerateData();
    }

    @Test
    @DisplayName("Удаление курьера")
    public void deleteCourierValid(){

        courierId = scooterGenerateData.createRandomCourier();

        assertThat("Id курьера некоректный", courierId, is(not(0)));

        boolean isCourierDeleted = scooterApiClient.deleteCourier(courierId).assertThat().statusCode(200).extract().path("ok");
        assertTrue(isCourierDeleted, "Курьер не удален");
    }

    @Test
    @DisplayName("Удаление курьера с несуществующим id")
    public void deleteCourierWithInvalidId(){
        courierId = 0;
        String message = "Курьера с таким id нет";
        String courierDeleted = scooterApiClient.deleteCourier(courierId).assertThat().statusCode(404).extract().path("message");
        assertThat("Сообщение о удалении курьера некорректно", courierDeleted, containsString(message));
    }

    @Test
    @DisplayName("Удаление курьера без id")
    public void deleteCourierWithoutId(){
        String message = "Недостаточно данных для удаления курьера";
        String courierDeleted = given()
                .header("Content-type", "application/json")
                .when()
                .delete(Endpoints.BASE_URL + Endpoints.COURIER_URL)
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");
        assertThat("Сообщение о недостаточности данных некорректно", courierDeleted, containsString(message));
    }
}
