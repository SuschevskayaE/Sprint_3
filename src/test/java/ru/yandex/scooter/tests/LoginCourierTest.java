package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.utils.ScooterGenerateData;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginCourierTest {
    private ScooterApiClient scooterApiClient;
    private ScooterGenerateData scooterGenerateData;
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
        scooterGenerateData = new ScooterGenerateData();
        courier = Courier.getRandom();

        boolean courierCreated = scooterGenerateData.createCourier(courier);

        assertTrue(courierCreated, "Курьер не создан");
    }

    @Test
    @DisplayName("Авторизация курьера")
    public void loginCourierValid(){
        courierId = scooterApiClient.loginCourier(CourierCredentials.getCredentials(courier))
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");

        assertThat("Id курьера некоректный", courierId, is(not(0)));

        scooterApiClient.deleteCourier(courierId).assertThat().statusCode(200);
    }

    @Test
    @DisplayName("Авторизация курьера с невалидными данными")
    public void loginCourierInvalid(){
        CourierCredentials courierCredentials = new CourierCredentials("invalidLogin","***");
        String message = "Учетная запись не найдена";

        String errorMessage = scooterApiClient.loginCourier(courierCredentials)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        assertThat("Сообщение об ошибке некорректно", errorMessage, containsString(message));
    }

    @Test
    @DisplayName("Авторизация курьера с пустым логином")
    public void loginCourierEmptyLogin(){
        CourierCredentials courierCredentials = new CourierCredentials("","***");
        String message = "Недостаточно данных для входа";

        String errorMessage = scooterApiClient.loginCourier(courierCredentials)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertThat("Сообщение об ошибке некорректно", errorMessage, containsString(message));
    }
}
