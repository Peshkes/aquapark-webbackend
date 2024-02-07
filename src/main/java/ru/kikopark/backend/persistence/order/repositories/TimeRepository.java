package ru.kikopark.backend.persistence.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kikopark.backend.persistence.order.entities.TimeEntity;

public interface TimeRepository extends JpaRepository<TimeEntity, Integer> {
    TimeEntity getTimeEntityByTimeId(Integer id);
}
