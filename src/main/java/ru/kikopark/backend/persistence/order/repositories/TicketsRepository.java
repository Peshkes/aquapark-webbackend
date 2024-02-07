package ru.kikopark.backend.persistence.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kikopark.backend.persistence.order.entities.TicketEntity;

import java.util.List;

public interface TicketsRepository extends JpaRepository<TicketEntity, Integer> {
    List<TicketEntity> getTicketEntitiesByTypeId(Integer typeId);
}
