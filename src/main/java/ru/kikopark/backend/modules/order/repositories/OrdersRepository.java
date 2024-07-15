package ru.kikopark.backend.modules.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.modules.order.entities.OrderEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdersRepository extends JpaRepository<OrderEntity, Integer> {
    Optional<OrderEntity> getOrderEntityByOrderId(UUID id);
    @Query("SELECT o.status.name FROM OrderEntity o WHERE o.orderId = :id")
    String getStatusNameById(Integer id);


}
