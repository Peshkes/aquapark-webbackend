package ru.kikopark.backend.modules.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.modules.order.entities.CouponEntity;

import java.util.UUID;

@Repository
public interface CouponsRepository extends JpaRepository<CouponEntity, Integer> {
    @Query("SELECT c FROM CouponEntity c WHERE c.institutionId = :institutionId AND c.couponCode = :couponCode ORDER BY c.expirationDate DESC")
    CouponEntity findTopByCouponCodeOrderByExpirationDateDesc(String couponCode, UUID institutionId);
}
