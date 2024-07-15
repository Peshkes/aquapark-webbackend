package ru.kikopark.backend.modules.order.repositories;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kikopark.backend.modules.order.entities.TypeEntity;

import java.util.List;

@CacheConfig(cacheNames = "typeRepository")
public interface TypesRepository extends JpaRepository<TypeEntity, Integer> {
    @Cacheable(key = "'findAll'")
    List<TypeEntity> findAll();
}
