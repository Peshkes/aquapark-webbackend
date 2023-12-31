package ru.kikopark.backend.model.order;

public class OrderStatus {
    private Integer status;

    public OrderStatus(Integer status) {
        this.status = status;
    }

    public OrderStatus() {
    }

    public Integer getStatus() {
        return status;
    }
}
