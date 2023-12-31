package ru.kikopark.backend.persistence.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.persistence.order.entities.OrderEntity;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntity, Integer> {
    OrderEntity getOrderEntityByOrderId(Integer id);
    @Query("SELECT s.name FROM OrderEntity o JOIN StatusEntity s ON o.statusId = s.statusId WHERE o.orderId = :id")
    String getStatusNameById(Integer id);
}
