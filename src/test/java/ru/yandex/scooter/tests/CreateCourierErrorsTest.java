package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.Courier;

import static org.junit.Assert.assertTrue;

public class CreateCourierErrorsTest {

    private CourierApiClient courierApiClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierApiClient = new CourierApiClient();
    }

    @Test
    @DisplayName("Создание курьера с пустым логином и паролем")
    public void createCourierWithoutCredentialsError() {
        courier = new Courier("", "", "Вася");
        String message = "Недостаточно данных для создания учетной записи";

        String courierWithoutCredentials = courierApiClient.createCourier(courier)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertTrue("Сообщение о создании курьера некорректно", courierWithoutCredentials.contains(message));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordError() {
        courier = new Courier(RandomStringUtils.randomAlphabetic(10), "", RandomStringUtils.randomAlphabetic(10));
        String message = "Недостаточно данных для создания учетной записи";

        String courierWithoutCredentials = courierApiClient.createCourier(courier)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertTrue("Сообщение о создании курьера некорректно", courierWithoutCredentials.contains(message));
    }
}
