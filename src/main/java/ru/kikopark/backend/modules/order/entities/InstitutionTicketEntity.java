package ru.kikopark.backend.modules.order.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "institution_tickets", schema = "public")
public class InstitutionTicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "institution_tickets_id", updatable = false, nullable = false)
    private UUID institutionTicketId;
    @Column(name = "institution_id")
    private UUID institutionId;
    @ManyToOne
    @JoinColumn(name = "ticket_id", referencedColumnName = "ticket_id")
    private TicketEntity ticket;
    private Integer price;
    @Column(name = "is_active")
    private Boolean isActive;

    public InstitutionTicketEntity(UUID institutionId, TicketEntity ticket, Integer price) {
        this.institutionId = institutionId;
        this.ticket = ticket;
        this.price = price;
        this.isActive = true;
    }
}
