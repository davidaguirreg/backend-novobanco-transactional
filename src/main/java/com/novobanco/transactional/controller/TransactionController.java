package com.novobanco.transactional.controller;

import com.novobanco.transactional.dto.request.DepositRequest;
import com.novobanco.transactional.dto.request.TransferRequest;
import com.novobanco.transactional.dto.request.WithdrawalRequest;
import com.novobanco.transactional.dto.response.MovementResponse;
import com.novobanco.transactional.dto.response.TransactionResponse;
import com.novobanco.transactional.service.AccountService;
import com.novobanco.transactional.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transacciones")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping("/deposito")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/retiro")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawalRequest request) {
        TransactionResponse response = transactionService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transferencia")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.transfer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuentas/{cuentaId}/movimientos")
    public ResponseEntity<Page<MovementResponse>> getMovements(
            @PathVariable Long cuentaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovementResponse> response = accountService.getAccountMovements(cuentaId, pageable);
        return ResponseEntity.ok(response);
    }
}