package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.utils.ScooterGenerateCurierData;

import static org.junit.Assert.assertTrue;

public class CreateCourierTest {

    private CourierApiClient courierApiClient;
    private ScooterGenerateCurierData scooterGenerateCurierData;
    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
        scooterGenerateCurierData = new ScooterGenerateCurierData();
    }

    @After
    public void tearDown() {
        courierId = scooterGenerateCurierData.loginCourier(courier);
        boolean isCourierDeleted = scooterGenerateCurierData.deleteCourier(courierId);
        assertTrue("Курьер не удален", isCourierDeleted);
    }

    @Test
    @DisplayName("Создание курьера")
    public void createCourierValidCredentials() {
        courier = Courier.getRandom();

        boolean courierCreated = courierApiClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");

        assertTrue("Курьер не создан", courierCreated);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createSecondIsSameCourierError() {
        courier = Courier.getRandom();
        String message = "Этот логин уже используется";

        boolean courierCreated = scooterGenerateCurierData.createCourier(courier);

        String secondCourierCreated = courierApiClient.createCourier(courier)
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");

        assertTrue("Сообщение о создании второго такого же курьера некорректно", secondCourierCreated.contains(message));
    }

    @Test
    @DisplayName("Создание курьера с повторяющимся логином")
    public void createSecondIsSameLoginError() {
        courier = Courier.getRandom();
        Courier secondCourier = new Courier(courier.login, RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
        String message = "Этот логин уже используется";

        boolean courierCreated = scooterGenerateCurierData.createCourier(courier);

        String secondCourierCreated = courierApiClient.createCourier(secondCourier)
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");

        assertTrue("Первый курьер не создан", courierCreated);
        assertTrue("Сообщение о создании второго курьера некорректно", secondCourierCreated.contains(message));
    }
}
