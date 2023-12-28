package ru.kikopark.backend.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity getAccountEntitiesByUserId(Integer id);
//    @Query("SELECT (a.name, r.roleName) "
//            + "FROM AccountEntity a INNER JOIN RoleEntity r ON AccountEntity.roleId = RoleEntity.roleId")
//    AccountEntity getAccountEntityByEmailAndPassword(String email, String password);
}
