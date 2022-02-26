package ru.yandex.scooter.api.models;

public class CourierCredentials {

    public String login;
    public String password;

    public CourierCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static CourierCredentials getCredentials(Courier courier) {
        return new CourierCredentials(courier.login, courier.password);
    }

    @Override
    public String toString() {
        return "CourierCredentials {" +
                "login:" + login + "," +
                "password:" + password + "}";
    }
}
