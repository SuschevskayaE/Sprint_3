package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.utils.ScooterGenerateOrderData;

import static org.junit.Assert.assertEquals;
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

        ValidatableResponse response = courierApiClient.loginCourier(courierCredentials);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals("statusCode неверный", 404, statusCode);
        assertTrue("Сообщение об ошибке некорректно", errorMessage.contains(message));
    }

    @Test
    @DisplayName("Авторизация курьера с пустым логином")
    public void loginCourierEmptyLogin() {
        CourierCredentials courierCredentials = new CourierCredentials("", "***");
        String message = "Недостаточно данных для входа";

        ValidatableResponse response = courierApiClient.loginCourier(courierCredentials);
        int statusCode = response.extract().statusCode();
        String errorMessage = response.extract().path("message");

        assertEquals("statusCode неверный", 400, statusCode);
        assertTrue("Сообщение об ошибке некорректно", errorMessage.contains(message));
    }
}
