package ru.yandex.scooter.api.utils;

import ru.yandex.scooter.api.client.CourierApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class ScooterGenerateCurierData {
    private CourierApiClient courierApiClient = new CourierApiClient();
    private int courierId;
    private Courier courier;

    public int createRandomCourier() {
        courier = Courier.getRandom();
        boolean courierCreated = courierApiClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        courierId = courierApiClient.loginCourier(CourierCredentials.getCredentials(courier))
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");

        assertTrue("Курьер не создан", courierCreated);
        assertNotEquals("Id курьера некоректный", 0, courierId);

        return courierId;
    }

    public boolean deleteCourier(int courierId) {
        return courierApiClient.deleteCourier(courierId).assertThat().statusCode(200).extract().path("ok");
    }

    public int loginCourier(Courier courier) {
        courierId = courierApiClient.loginCourier(CourierCredentials.getCredentials(courier))
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");

        assertNotEquals("Id курьера некоректный", 0, courierId);
        return courierId;
    }

    public boolean createCourier(Courier courier) {
        return courierApiClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
    }
}
