package ru.kikopark.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kikopark.backend.configs.SecurityConfig;
import ru.kikopark.backend.exeptions.AppError;
import ru.kikopark.backend.model.authentication.AccountResponse;
import ru.kikopark.backend.model.authentication.AuthenticationRequest;
import ru.kikopark.backend.model.authentication.AuthenticationResponse;
import ru.kikopark.backend.persistence.authentication.entities.AccountEntity;
import ru.kikopark.backend.persistence.authentication.entities.RoleEntity;
import ru.kikopark.backend.service.AuthenticationService;
import ru.kikopark.backend.utils.JwtService;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {
    AuthenticationService authenticationService;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @GetMapping("/employee/get-account")
    public AccountResponse getAccountEntityByEmailAndPassword(@RequestParam String email, @RequestParam String password) {
        return authenticationService.getAccountEntityByEmailAndPassword(email, password);
    }

    @PostMapping("/guest/create-account")
    public ResponseEntity<AccountResponse> addAccount(HttpEntity<String> httpEntity) {
        Optional<AccountEntity> insertionSuccess = authenticationService.addNewAccount(httpEntity);
        String accountName = null;
        String accountRole = null;
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        if (insertionSuccess.isPresent()) {
            accountName = insertionSuccess.get().getName();
            RoleEntity roleEntity = authenticationService.getRoleEntityById(insertionSuccess.get().getRoleId());
            accountRole = roleEntity.getRoleName();
            httpStatus = HttpStatus.OK;
        }
        AccountResponse accountResponse = new AccountResponse(accountName, accountRole);
        return new ResponseEntity<>(accountResponse, httpStatus);
    }

    @PostMapping("/guest/authentication")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            UserDetails userDetails = authenticationService.loadUserByUsername(authenticationRequest.getUsername());
            if (userDetails != null && SecurityConfig.passwordEncoder().matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));

                String accessToken = jwtService.generateAccessToken(userDetails);
                String refreshToken = jwtService.generateRefreshToken(userDetails);

                String[] roles = userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).distinct().toArray(String[]::new);

                AuthenticationResponse authenticationResponse = new AuthenticationResponse(accessToken, refreshToken, roles);
                return ResponseEntity.ok(authenticationResponse);
            } else {
                return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Ошибка аутентификации"), HttpStatus.UNAUTHORIZED);
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Ошибка аутентификации"), HttpStatus.UNAUTHORIZED);
        }
    }

}