package ru.yandex.scooter.api.utils;

import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.models.Courier;
import ru.yandex.scooter.api.models.CourierCredentials;
import ru.yandex.scooter.api.models.Order;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScooterGenerateData {
    private ScooterApiClient scooterApiClient = new ScooterApiClient();
    private int courierId;
    private int track;
    private int idOrder;
    private Order order;
    private Courier courier;

    public int createRandomCourier(){
        courier = Courier.getRandom();
        boolean courierCreated = scooterApiClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
        assertTrue(courierCreated, "Курьер не создан");

        courierId = scooterApiClient.loginCourier(CourierCredentials.getCredentials(courier))
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");
        assertThat("Id курьера некоректный", courierId, is(not(0)));

        return courierId;
    }

    public int createRandomOrder(){
        order = Order.getRandom();

        track = scooterApiClient.createOrders(order)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");
        assertThat("Track некоректный", track, is(not(0)));

        idOrder = scooterApiClient.getOrdersByTrack(track)
                .assertThat()
                .statusCode(200)
                .extract()
                .path("order.id");
        assertThat("Id заказа некоректный", idOrder, is(not(0)));

        return idOrder;
    }

    public boolean deleteCourier(int courierId){
         return scooterApiClient.deleteCourier(courierId).assertThat().statusCode(200).extract().path("ok");
    }

    public boolean finishOrder(int idOrder){
        return scooterApiClient.finishOrders(idOrder).assertThat().statusCode(200).extract().path("ok");
    }

    public boolean cancelOrder(int trackOrder){
        return scooterApiClient.cancelOrders(trackOrder).assertThat().statusCode(200).extract().path("ok");
    }

    public int loginCourier(Courier courier){
        courierId = scooterApiClient.loginCourier(CourierCredentials.getCredentials(courier))
                .assertThat()
                .statusCode(200)
                .extract()
                .path("id");
        assertThat("Id курьера некоректный", courierId, is(not(0)));
        return courierId;
    }

    public boolean createCourier(Courier courier){
        return scooterApiClient.createCourier(courier)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("ok");
    }
}
