package ru.yandex.scooter.tests;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.api.client.ScooterApiClient;
import ru.yandex.scooter.api.models.Order;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private ScooterApiClient scooterApiClient;
    private int track;

    private final String[] color;

    public CreateOrderTest(String[] color){
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] getColorList(){
        return new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"GREY", "BLACK"}},
                {new String[0]}

        };
    }

    @Before
    public void setUp() {
        scooterApiClient = new ScooterApiClient();
    }

    @Test
    @DisplayName("Создание заказа")
    public void createOrderWithColors(){
        Order order = Order.getRandom();
        order.setColor(color);

        track = scooterApiClient.createOrders(order)
                .assertThat()
                .statusCode(201)
                .extract()
                .path("track");

        assertThat("Track некоректный", track, is(not(0)));
    }

}
