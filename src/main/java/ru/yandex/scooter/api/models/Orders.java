package ru.yandex.scooter.api.models;

public class Orders {
    public OrderResponse[] orders;

    public OrderResponse[] getOrders() {
        return orders;
    }

    public void setOrders(OrderResponse[] orders) {
        this.orders = orders;
    }
}
