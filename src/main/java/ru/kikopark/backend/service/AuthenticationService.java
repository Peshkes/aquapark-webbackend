package ru.kikopark.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kikopark.backend.configs.SecurityConfig;
import ru.kikopark.backend.model.authentication.AccountRequest;
import ru.kikopark.backend.model.authentication.AccountResponse;
import ru.kikopark.backend.persistence.authentication.entities.AccountEntity;
import ru.kikopark.backend.persistence.authentication.entities.RoleEntity;
import ru.kikopark.backend.persistence.authentication.repositories.AccountRepository;
import ru.kikopark.backend.persistence.authentication.repositories.RoleRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
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
    @Transactional
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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieve the user details from the database based on the email
        Optional<AccountEntity> optionalAccount = Optional.ofNullable(accountRepository.findAccountEntityByEmail(email));

        return optionalAccount.map(this::buildUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

//    utils
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
        BCryptPasswordEncoder bCryptPasswordEncoder = SecurityConfig.passwordEncoder();
        return new AccountEntity(
                accountRequest.getRoleId(),
                accountRequest.getName(),
                bCryptPasswordEncoder.encode(accountRequest.getPassword()),
                accountRequest.getEmail());
    }

    private UserDetails buildUserDetails(AccountEntity accountEntity) {
        RoleEntity role = roleRepository.findRoleEntityByRoleId(accountEntity.getRoleId());
        RoleEntity[] roles = {role};

        // Create a list of GrantedAuthority based on the user's roles
        List<GrantedAuthority> authorities = Arrays.stream(roles)
                .map(roleEntity -> new SimpleGrantedAuthority(roleEntity.getRoleName()))
                .collect(Collectors.toList());

        // Build and return the UserDetails object
        return new User(accountEntity.getEmail(), accountEntity.getPassword(), authorities) {};
    }
}
