package ru.kikopark.backend.modules.order.repositories;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.modules.order.entities.PaidPersonEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@CacheConfig(cacheNames = "PaidPersonRepository")
public interface PaidPersonRepository extends JpaRepository<PaidPersonEntity, UUID> {

    @Cacheable(key = "'findAll'")
    List<PaidPersonEntity> findAll();

    @Cacheable(key = "'getByEmail:' + #email")
    Optional<PaidPersonEntity> getPaidPersonEntityByEmail(String email);
}
