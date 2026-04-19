package com.novobanco.transactional.controller;

import com.novobanco.transactional.dto.request.CreateAccountRequest;
import com.novobanco.transactional.dto.response.AccountResponse;
import com.novobanco.transactional.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuentas")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountInfo(@PathVariable("id") Long id) {
        AccountResponse response = accountService.getAccountInfo(id);
        return ResponseEntity.ok(response);
    }
}