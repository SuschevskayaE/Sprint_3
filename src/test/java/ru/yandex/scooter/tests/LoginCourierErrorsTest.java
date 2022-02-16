package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.utils.ScooterGenerateOrderData;

import static org.junit.Assert.assertTrue;

public class LoginCourierErrorsTest {
    private CourierApiClient courierApiClient;
    private ScooterGenerateOrderData scooterGenerateOrderData;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        scooterGenerateOrderData = new ScooterGenerateOrderData();
    }

    @Test
    @DisplayName("Авторизация курьера с невалидными данными")
    public void loginCourierInvalid() {
        CourierCredentials courierCredentials = new CourierCredentials("invalidLogin", "***");
        String message = "Учетная запись не найдена";

        String errorMessage = courierApiClient.loginCourier(courierCredentials)
                .assertThat()
                .statusCode(404)
                .extract()
                .path("message");

        assertTrue("Сообщение об ошибке некорректно", errorMessage.contains(message));
    }

    @Test
    @DisplayName("Авторизация курьера с пустым логином")
    public void loginCourierEmptyLogin() {
        CourierCredentials courierCredentials = new CourierCredentials("", "***");
        String message = "Недостаточно данных для входа";

        String errorMessage = courierApiClient.loginCourier(courierCredentials)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertTrue("Сообщение об ошибке некорректно", errorMessage.contains(message));
    }
}
