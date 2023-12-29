package ru.kikopark.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kikopark.backend.model.authentication.AccountResponse;
import ru.kikopark.backend.persistence.authentication.entities.AccountEntity;
import ru.kikopark.backend.persistence.authentication.entities.RoleEntity;
import ru.kikopark.backend.service.AuthenticationService;

import java.util.Optional;

@RestController
public class AuthenticationController {
    AuthenticationService authenticationService;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @GetMapping("/accountbyid")
    public AccountEntity getAccount(@RequestParam Integer id){
        return authenticationService.getAccountById(id);
    }

    @GetMapping("/account")
    public AccountResponse getAccountEntityByEmailAndPassword(@RequestParam String email, @RequestParam String password){
        return authenticationService.getAccountEntityByEmailAndPassword(email, password);
    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> addAccount(HttpEntity<String> httpEntity){
        Optional<AccountEntity> insertionSuccess = authenticationService.addNewAccount(httpEntity);
        String accountName = null;
        String accountRole = null;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (insertionSuccess.isPresent()){
            accountName = insertionSuccess.get().getName();
            RoleEntity roleEntity = authenticationService.getRoleEntityById(insertionSuccess.get().getRoleId());
            accountRole = roleEntity.getRoleName();
            httpStatus = HttpStatus.OK;
        }
        AccountResponse accountResponse = new AccountResponse(accountName, accountRole);
        return new ResponseEntity<>(accountResponse, httpStatus);
    }
}
