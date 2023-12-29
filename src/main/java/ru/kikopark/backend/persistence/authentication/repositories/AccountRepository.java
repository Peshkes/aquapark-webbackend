package ru.kikopark.backend.persistence.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kikopark.backend.model.authentication.AccountResponse;
import ru.kikopark.backend.persistence.authentication.entities.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity getAccountEntitiesByUserId(Integer id);

    @Query("SELECT new ru.kikopark.backend.model.authentication.AccountResponse(a.name, r.roleName) "
            + "FROM AccountEntity a INNER JOIN RoleEntity r ON a.roleId = r.roleId "
            + "WHERE a.email = :email AND a.password = :password")
    AccountResponse getAccountEntityByEmailAndPassword(String email, String password);
}
