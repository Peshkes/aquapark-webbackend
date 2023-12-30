package ru.kikopark.backend.persistence.order.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items", schema = "public")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer orderItemId;
    @Column(name = "order_id")
    private Integer orderId;
    @Column(name = "ticket_id")
    private Integer ticketId;
    private Integer count;

    public OrderItemEntity(Integer orderId, Integer ticketId, Integer count) {
        this.orderId = orderId;
        this.ticketId = ticketId;
        this.count = count;
    }

    public OrderItemEntity(){};

    public Integer getOrderItemId() {
        return orderItemId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public Integer getCount() {
        return count;
    }
}
