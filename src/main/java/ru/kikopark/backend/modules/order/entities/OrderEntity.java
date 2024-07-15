package ru.kikopark.backend.modules.order.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders", schema = "public")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false)
    private UUID orderId;
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    private StatusEntity status;
    @Column(name = "date_paid")
    private Timestamp datePaid;
    @Setter
    @Column(name = "date_changed")
    private Timestamp dateChanged;
    @Column(name = "institution_id")
    private UUID institutionId;
    private double sum;
    @ManyToOne
    @JoinColumn(name = "paid_person_id", referencedColumnName = "person_id")
    private PaidPersonEntity paidPersonEntity;

    public OrderEntity(StatusEntity status, UUID institutionId, double sum, PaidPersonEntity paidPersonEntity) {
        this.sum = sum;
        this.status = status;
        this.datePaid = new Timestamp(System.currentTimeMillis());
        this.dateChanged = null;
        this.institutionId = institutionId;
        this.paidPersonEntity = paidPersonEntity;
    }

    public void setStatus(StatusEntity statusEntity) {
        this.status = statusEntity;
        this.dateChanged = new Timestamp(System.currentTimeMillis());
    }
}
