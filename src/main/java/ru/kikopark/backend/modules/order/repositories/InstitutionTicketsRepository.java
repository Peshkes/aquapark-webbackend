package ru.kikopark.backend.modules.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.modules.order.entities.InstitutionTicketEntity;
import ru.kikopark.backend.modules.order.entities.TypeEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface InstitutionTicketsRepository extends JpaRepository<InstitutionTicketEntity, UUID> {
    InstitutionTicketEntity getInstitutionTicketEntityByInstitutionTicketId(UUID id);
    List<InstitutionTicketEntity> findInstitutionTicketEntitiesByInstitutionId(UUID id);
    List<InstitutionTicketEntity> findByIsActiveTrueAndTicket_Type(TypeEntity type);
    List<InstitutionTicketEntity> findByIsActiveTrueAndTicket_TypeAndInstitutionId(TypeEntity type, UUID institutionId);
    InstitutionTicketEntity findInstitutionTicketEntityByTicket_TicketId(Integer id);
}
