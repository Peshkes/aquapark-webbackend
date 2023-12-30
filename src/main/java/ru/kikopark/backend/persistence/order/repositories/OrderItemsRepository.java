package ru.kikopark.backend.persistence.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.persistence.order.entities.OrderItemEntity;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItemEntity, Integer> {
    OrderItemEntity getOrderItemEntitiesByOrderId(Integer orderId);
}
