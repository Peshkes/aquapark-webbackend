package ru.kikopark.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kikopark.backend.model.AccountResponse;
import ru.kikopark.backend.persistence.AccountEntity;
import ru.kikopark.backend.service.AccountService;

import java.util.Optional;

@RestController
public class AccountController {
    AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService){
        this.accountService = accountService;
    }

    @GetMapping("/accountbyid")
    public AccountEntity getAccount(@RequestParam Integer id){
        return accountService.getAccountById(id);
    }
//    @GetMapping("/account")
//    public AccountEntity getAccountEntityByEmailAndPassword(@RequestParam String email, @RequestParam String password){
//        return accountService.getAccountEntityByEmailAndPassword(email, password);
//    }

    @PostMapping("/create-account")
    public ResponseEntity<AccountResponse> addAccount(HttpEntity<String> httpEntity){
        Optional<AccountEntity> insertionSuccess = accountService.addNewAccount(httpEntity);
        String accountName = null;
        String accountRole = null;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (insertionSuccess.isPresent()){
            accountName = insertionSuccess.get().getName();
            accountRole = insertionSuccess.get().getRoleId().toString(); //исправить!
            httpStatus = HttpStatus.OK;
        }
        AccountResponse accountResponse = new AccountResponse(accountName, accountRole);
        return new ResponseEntity<>(accountResponse, httpStatus);
    }
}
