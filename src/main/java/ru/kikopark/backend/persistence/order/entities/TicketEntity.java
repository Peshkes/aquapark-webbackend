package ru.kikopark.backend.persistence.order.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tickets", schema = "public")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    Integer ticketId;
    String name;
    Integer price;
    @Column(name = "type_id")
    Integer typeId;
    @Column(name = "time_id")
    Integer timeId;

    public Integer getTicketId() {
        return ticketId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public Integer getTimeId() {
        return timeId;
    }
}
