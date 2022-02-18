package ru.yandex.scooter.api.utils;

import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;

import static org.junit.Assert.*;

public class ScooterGenerateCurierData {
    private CourierApiClient courierApiClient = new CourierApiClient();
    private int courierId;
    private Courier courier;

    public int createRandomCourier() {
        courier = Courier.getRandom();

        ValidatableResponse response = courierApiClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean courierCreated = response.extract().path("ok");

        assertEquals("statusCode неверный", 201, statusCode);
        assertTrue("Курьер не создан", courierCreated);

        ValidatableResponse responseCourier = courierApiClient.loginCourier(CourierCredentials.getCredentials(courier));
        int statusCodeCourier = responseCourier.extract().statusCode();
        courierId = responseCourier.extract().path("id");

        assertEquals("statusCode неверный", 200, statusCodeCourier);
        assertNotEquals("Id курьера некоректный", 0, courierId);

        return courierId;
    }

    public boolean deleteCourier(int courierId) {

        ValidatableResponse response = courierApiClient.deleteCourier(courierId);
        int statusCode = response.extract().statusCode();
        boolean errorMessage = response.extract().path("ok");
        assertEquals("statusCode неверный", 200, statusCode);

        return errorMessage;
    }

    public int loginCourier(Courier courier) {
        ValidatableResponse responseCourier = courierApiClient.loginCourier(CourierCredentials.getCredentials(courier));
        int statusCodeCourier = responseCourier.extract().statusCode();
        courierId = responseCourier.extract().path("id");

        assertEquals("statusCode неверный", 200, statusCodeCourier);
        assertNotEquals("Id курьера некоректный", 0, courierId);
        return courierId;
    }

    public boolean createCourier(Courier courier) {

        ValidatableResponse response = courierApiClient.createCourier(courier);
        int statusCode = response.extract().statusCode();
        boolean errorMessage = response.extract().path("ok");
        assertEquals("statusCode неверный", 201, statusCode);

        return errorMessage;
    }
}
