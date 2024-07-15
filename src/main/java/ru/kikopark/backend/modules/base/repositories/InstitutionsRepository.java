package ru.kikopark.backend.modules.base.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.modules.base.entities.InstitutionEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InstitutionsRepository extends JpaRepository<InstitutionEntity, UUID> {
    @Query("SELECT i.institutionId FROM InstitutionEntity i")
    UUID getInstitutionId();

    Optional<InstitutionEntity> findByInstitutionId(UUID institutionId);
}
