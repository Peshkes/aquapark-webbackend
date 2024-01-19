package ru.kikopark.backend.persistence.order.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "orders", schema = "public")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "full_name")
    private String fullName;
    private Integer price;
    @Column(name = "status_id")
    private Integer statusId;
    @Column(name = "date_paid")
    private Timestamp datePaid;
    @Column(name = "date_changed")
    private Timestamp dateChanged;

    public OrderEntity(String fullName, Integer price) {
        this.fullName = fullName;
        this.price = price;
        this.statusId = 1;
        this.datePaid = new Timestamp(System.currentTimeMillis());
        this.dateChanged = null;
    }

    public OrderEntity() {
    }

    public Integer getOrderId() {
        return orderId;
    }

    public String getFullName() {
        return fullName;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public Timestamp getDatePaid() {
        return datePaid;
    }

    public Timestamp getDateChanged() {
        return dateChanged;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public void setDateChanged(Timestamp dateChanged) {
        this.dateChanged = dateChanged;
    }
}
