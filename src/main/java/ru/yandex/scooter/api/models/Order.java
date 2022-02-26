package ru.yandex.scooter.api.models;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Order {
    public String firstName;
    public String lastName;
    public String address;
    public String metroStation;
    public String phone;
    public int rentTime;
    public String deliveryDate;
    public String comment;
    public String[] color;

    public Order(String firstName, String lastName, String address, String metroStation, String phone, int rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    public static Order getRandom() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String address = faker.address().fullAddress();
        String metroStation = faker.address().streetName();
        String phone= faker.phoneNumber().cellPhone();
        int rentTime = faker.number().numberBetween(0, 367);
        String deliveryDate = new SimpleDateFormat("yyyy-MM-dd").format(faker.date().future(2000, TimeUnit.DAYS));
        String comment = RandomStringUtils.randomAlphabetic(100);
        String[] color = new String[0];

        return new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);
    }

    public void setColor(String[] color){ this.color = color;}

    @Override
    public String toString() {
        return "Order {" +
                "firstName:" + firstName + "," +
                "lastName:" + lastName + "," +
                "address:" + address + "," +
                "metroStation:" + metroStation + "," +
                "phone:" + phone + "," +
                "rentTime:" + rentTime + "," +
                "deliveryDate:" + deliveryDate + "," +
                "comment:" + comment + "," +
                "color:" + color + "}";
    }

}
