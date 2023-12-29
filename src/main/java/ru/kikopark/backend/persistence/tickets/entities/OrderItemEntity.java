package ru.kikopark.backend.persistence.tickets.entities;

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
}
