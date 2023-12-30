package ru.kikopark.backend.persistence.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.persistence.order.entities.OrderEntity;

@Repository
public interface OrderRepository  extends JpaRepository<OrderEntity, Integer> {
    OrderEntity getOrderEntityByOrderId(Integer id);
}
