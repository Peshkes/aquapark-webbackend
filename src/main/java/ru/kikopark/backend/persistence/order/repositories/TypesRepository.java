package ru.kikopark.backend.persistence.order.repositories;

import jakarta.annotation.Nullable;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kikopark.backend.persistence.order.entities.TypeEntity;

import java.util.List;

public interface TypesRepository extends JpaRepository<TypeEntity, Integer> {
    List<TypeEntity> findAll();
}
