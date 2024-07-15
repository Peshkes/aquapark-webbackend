package ru.kikopark.backend.modules.base.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.modules.base.entities.InstitutionEntity;
import ru.kikopark.backend.modules.base.repositories.InstitutionsRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BaseService {

    InstitutionsRepository institutionsRepository;

    public InstitutionEntity[] getInstitutions() {
        return institutionsRepository.findAll().toArray(new InstitutionEntity[0]);
    }

    @Transactional
    public Optional<InstitutionEntity> addInstitutionIfNotExists(InstitutionEntity institution) {
        Optional<InstitutionEntity> existingInstitution = institutionsRepository.findByInstitutionId(institution.getInstitutionId());
        if (existingInstitution.isPresent()) {
            return Optional.empty();
        } else {
            InstitutionEntity savedInstitution = institutionsRepository.save(institution);
            return Optional.of(savedInstitution);
        }
    }
}
