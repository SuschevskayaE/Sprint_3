package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.utils.ScooterGenerateCurierData;

import static org.junit.Assert.*;

public class LoginCourierTest {
    private CourierApiClient courierApiClient;
    private ScooterGenerateCurierData scooterGenerateCurierData;
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        scooterGenerateCurierData = new ScooterGenerateCurierData();
        courier = Courier.getRandom();

        boolean courierCreated = scooterGenerateCurierData.createCourier(courier);
        assertTrue("Курьер не создан", courierCreated);
    }

    @After
    public void tearDown() {
        boolean isCourierDeleted = scooterGenerateCurierData.deleteCourier(courierId);
        assertTrue("Курьер не удален", isCourierDeleted);
    }

    @Test
    @DisplayName("Авторизация курьера")
    public void loginCourierValid() {

        ValidatableResponse response = courierApiClient.loginCourier(CourierCredentials.getCredentials(courier));
        int statusCode = response.extract().statusCode();
        courierId = response.extract().path("id");

        assertEquals("statusCode неверный", 200, statusCode);
        assertNotEquals("Id курьера некоректный", 0, courierId);
    }
}
