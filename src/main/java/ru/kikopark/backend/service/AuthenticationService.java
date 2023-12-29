package ru.kikopark.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.model.authentication.AccountRequest;
import ru.kikopark.backend.model.authentication.AccountResponse;
import ru.kikopark.backend.persistence.authentication.entities.AccountEntity;
import ru.kikopark.backend.persistence.authentication.entities.RoleEntity;
import ru.kikopark.backend.persistence.authentication.repositories.AccountRepository;
import ru.kikopark.backend.persistence.authentication.repositories.RoleRepository;

import java.util.Optional;

@Service
public class AuthenticationService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    @Autowired
    public AuthenticationService(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    public AccountEntity getAccountById(Integer id) {
        return accountRepository.getAccountEntitiesByUserId(id);
    }

    public AccountResponse getAccountEntityByEmailAndPassword(String email, String password) {
        return accountRepository.getAccountEntityByEmailAndPassword(email, password);
    }

    public RoleEntity getRoleEntityById(int id){
        return roleRepository.findRoleEntityByRoleId(id);
    }

    public Optional<AccountEntity> addNewAccount(HttpEntity<String> account) {
        Optional<AccountEntity> addedAccount = Optional.empty();
        Optional<AccountRequest> accountRequest = jsonToAccount(account.getBody());

        if (accountRequest.isPresent()) {
            AccountEntity newAccount = accountEntityMapper(accountRequest.get());
            AccountEntity returnedUser = accountRepository.save(newAccount);
            addedAccount = Optional.of(returnedUser);
        }

        return addedAccount;
    }

    private Optional<AccountRequest> jsonToAccount(String json) {
        ObjectMapper mapper = new ObjectMapper();
        Optional<AccountRequest> account = Optional.empty();
        try {
            AccountRequest mappedAccount = mapper.readValue(json, AccountRequest.class);
            account = Optional.of(mappedAccount);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return account;
    }

    private AccountEntity accountEntityMapper(AccountRequest accountRequest) {
        return new AccountEntity(accountRequest.getRoleId(), accountRequest.getName(), accountRequest.getPassword(), accountRequest.getEmail());
    }
}
