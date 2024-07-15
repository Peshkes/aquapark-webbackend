package ru.kikopark.backend.modules.base.controller;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import ru.kikopark.backend.modules.base.entities.InstitutionEntity;
import ru.kikopark.backend.modules.base.service.BaseService;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class BaseController {

    BaseService baseService;

    @GetMapping("/guest/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @GetMapping("/site/institutions")
    public InstitutionEntity[] getInstitutions() {
        return baseService.getInstitutions();
    }
    @GetMapping("/localserver/new-institution")
    public ResponseEntity<Void> addInstitutionIfNotExists(@RequestBody InstitutionEntity institutionEntity) {
        Optional<InstitutionEntity> addedInstitution = baseService.addInstitutionIfNotExists(institutionEntity);
        if (addedInstitution.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
