package ru.kikopark.backend.modules.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.modules.authentication.entities.RoleEntity;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findRoleEntityByRoleId(int roleId);
    RoleEntity findRoleEntityByRoleName(String name);
    List<RoleEntity> findAll();
}
