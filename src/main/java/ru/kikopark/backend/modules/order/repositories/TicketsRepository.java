package ru.kikopark.backend.modules.order.repositories;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kikopark.backend.modules.order.entities.TicketEntity;

import java.util.List;

@CacheConfig(cacheNames = "ticketRepository")
public interface TicketsRepository extends JpaRepository<TicketEntity, Integer> {
    @Cacheable(key = "'findTicketEntityByType_TypeId:' + #typeId")
    List<TicketEntity> findTicketEntityByType_TypeId(Integer typeId);
    @Cacheable(key = "'findTicketEntityByTicketId:' + #id")
    TicketEntity findTicketEntityByTicketId(Integer id);
}
