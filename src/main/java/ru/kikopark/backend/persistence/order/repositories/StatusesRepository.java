package ru.kikopark.backend.persistence.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.persistence.order.entities.StatusEntity;
@Repository
public interface StatusesRepository extends JpaRepository<StatusEntity, Integer> {
    StatusEntity getAllBy();
}
