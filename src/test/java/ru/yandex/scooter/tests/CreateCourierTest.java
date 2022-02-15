package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.utils.ScooterGenerateData;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateCourierTest {

    private ScooterApiClient scooterApiClient;
    private ScooterGenerateData scooterGenerateData;
    private int courierId;

    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
        scooterGenerateData = new ScooterGenerateData();
    }

    @Test
    @DisplayName("Создание курьера")
    public void createCourierValidCredentials(){
        Courier courier = Courier.getRandom();

        boolean courierCreated = scooterApiClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");

        assertTrue(courierCreated, "Курьер не создан");

        courierId = scooterGenerateData.loginCourier(courier);
        boolean isCourierDeleted = scooterGenerateData.deleteCourier(courierId);
        assertTrue(isCourierDeleted, "Курьер не удален");
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    public void createSecondIsSameCourierError(){
        Courier courier = Courier.getRandom();
        String message = "Этот логин уже используется";

        boolean courierCreated = scooterGenerateData.createCourier(courier);
        assertTrue(courierCreated, "Первый курьер не создан");

        courierId = scooterGenerateData.loginCourier(courier);

        String secondCourierCreated = scooterApiClient.createCourier(courier)
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");

        assertThat("Сообщение о создании второго такого же курьера некорректно", secondCourierCreated, containsString(message));

        boolean isCourierDeleted = scooterGenerateData.deleteCourier(courierId);
        assertTrue(isCourierDeleted, "Курьер не удален");
    }

    @Test
    @DisplayName("Создание курьера с пустым логином и паролем")
    public void createCourierWithoutCredentialsError(){
        Courier courier = new Courier("","","Вася");
        String message = "Недостаточно данных для создания учетной записи";

        String courierWithoutCredentials = scooterApiClient.createCourier(courier)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertThat("Сообщение о создании второго такого же курьера некорректно", courierWithoutCredentials, containsString(message));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    public void createCourierWithoutPasswordError(){
        Courier courier = new Courier(RandomStringUtils.randomAlphabetic(10),"",RandomStringUtils.randomAlphabetic(10));
        String message = "Недостаточно данных для создания учетной записи";

        String courierWithoutCredentials = scooterApiClient.createCourier(courier)
                .assertThat()
                .statusCode(400)
                .extract()
                .path("message");

        assertThat("Сообщение о создании второго такого же курьера некорректно", courierWithoutCredentials, containsString(message));

    }

    @Test
    @DisplayName("Создание курьера с повторяющимся логином")
    public void createSecondIsSameLoginError(){
        Courier courier = Courier.getRandom();
        Courier secondCourier = new Courier(courier.login,RandomStringUtils.randomAlphabetic(10),RandomStringUtils.randomAlphabetic(10));
        String message = "Этот логин уже используется";

        boolean courierCreated = scooterGenerateData.createCourier(courier);
        assertTrue(courierCreated, "Первый курьер не создан");

        courierId = scooterGenerateData.loginCourier(courier);

        String secondCourierCreated = scooterApiClient.createCourier(secondCourier)
                .assertThat()
                .statusCode(409)
                .extract()
                .path("message");

        assertThat("Сообщение о создании второго курьера некорректно", secondCourierCreated, containsString(message));

        boolean isCourierDeleted = scooterGenerateData.deleteCourier(courierId);
        assertTrue(isCourierDeleted, "Курьер не удален");
    }
}
