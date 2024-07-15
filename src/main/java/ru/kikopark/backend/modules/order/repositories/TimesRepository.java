package ru.kikopark.backend.modules.order.repositories;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kikopark.backend.modules.order.entities.TimeEntity;

import java.util.List;

@CacheConfig(cacheNames = "timeRepository")
public interface TimesRepository extends JpaRepository<TimeEntity, Integer> {
    @Cacheable(key = "'getTimeEntityByTimeId:' + #id")
    TimeEntity getTimeEntityByTimeId(Integer id);

    @Cacheable(key = "'findAll'")
    List<TimeEntity> findAll();
}
