package com.novobanco.transactional.service;

import com.novobanco.transactional.dto.request.DepositRequest;
import com.novobanco.transactional.dto.request.TransferRequest;
import com.novobanco.transactional.dto.request.WithdrawalRequest;
import com.novobanco.transactional.entity.Cuenta;
import com.novobanco.transactional.exception.InsufficientFundsException;
import com.novobanco.transactional.exception.InvalidTransactionException;
import com.novobanco.transactional.repository.CuentaRepository;
import com.novobanco.transactional.repository.MovimientoRepository;
import com.novobanco.transactional.repository.TransaccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private TransactionService transactionService;

    // ========================
    // DEPOSIT
    // ========================

    @Test
    void deposit_shouldIncreaseBalance() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123");
        cuenta.setSaldoDisponible(new BigDecimal("100"));
        cuenta.setEstado(Cuenta.EstadoCuenta.activa);

        DepositRequest request = new DepositRequest();
        request.setNumeroCuenta("123");
        request.setMonto(new BigDecimal("50"));

        when(cuentaRepository.findByNumeroCuentaForUpdate("123"))
                .thenReturn(Optional.of(cuenta));

        when(transaccionRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(movimientoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        transactionService.deposit(request);

        assertEquals(new BigDecimal("150"), cuenta.getSaldoDisponible());
        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void deposit_shouldFail_whenAccountNotFound() {
        DepositRequest request = new DepositRequest();
        request.setNumeroCuenta("999");
        request.setMonto(new BigDecimal("50"));

        when(cuentaRepository.findByNumeroCuentaForUpdate("999"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidTransactionException.class, () ->
                transactionService.deposit(request)
        );
    }

    // ========================
    // WITHDRAW
    // ========================

    @Test
    void withdraw_shouldDecreaseBalance() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123");
        cuenta.setSaldoDisponible(new BigDecimal("100"));
        cuenta.setEstado(Cuenta.EstadoCuenta.activa);

        WithdrawalRequest request = new WithdrawalRequest();
        request.setNumeroCuenta("123");
        request.setMonto(new BigDecimal("40"));

        when(cuentaRepository.findByNumeroCuentaForUpdate("123"))
                .thenReturn(Optional.of(cuenta));

        when(transaccionRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(movimientoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        transactionService.withdraw(request);

        assertEquals(new BigDecimal("60"), cuenta.getSaldoDisponible());
        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void withdraw_shouldThrowException_whenInsufficientFunds() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123");
        cuenta.setSaldoDisponible(new BigDecimal("50"));
        cuenta.setEstado(Cuenta.EstadoCuenta.activa);

        WithdrawalRequest request = new WithdrawalRequest();
        request.setNumeroCuenta("123");
        request.setMonto(new BigDecimal("100"));

        when(cuentaRepository.findByNumeroCuentaForUpdate("123"))
                .thenReturn(Optional.of(cuenta));

        assertThrows(InsufficientFundsException.class, () ->
                transactionService.withdraw(request)
        );
    }

    // ========================
    // TRANSFER
    // ========================

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() {
        Cuenta origen = new Cuenta();
        origen.setNumeroCuenta("123");
        origen.setSaldoDisponible(new BigDecimal("200"));
        origen.setEstado(Cuenta.EstadoCuenta.activa);

        Cuenta destino = new Cuenta();
        destino.setNumeroCuenta("456");
        destino.setSaldoDisponible(new BigDecimal("100"));
        destino.setEstado(Cuenta.EstadoCuenta.activa);

        TransferRequest request = new TransferRequest();
        request.setNumeroCuentaOrigen("123");
        request.setNumeroCuentaDestino("456");
        request.setMonto(new BigDecimal("50"));

        when(cuentaRepository.findByNumeroCuentaForUpdate("123"))
                .thenReturn(Optional.of(origen));

        when(cuentaRepository.findByNumeroCuentaForUpdate("456"))
                .thenReturn(Optional.of(destino));

        when(transaccionRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(movimientoRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        transactionService.transfer(request);

        assertEquals(new BigDecimal("150"), origen.getSaldoDisponible());
        assertEquals(new BigDecimal("150"), destino.getSaldoDisponible());

        verify(cuentaRepository).save(origen);
        verify(cuentaRepository).save(destino);
    }

    @Test
    void transfer_shouldFail_whenOriginAccountNotFound() {
        TransferRequest request = new TransferRequest();
        request.setNumeroCuentaOrigen("123");
        request.setNumeroCuentaDestino("456");
        request.setMonto(new BigDecimal("50"));

        when(cuentaRepository.findByNumeroCuentaForUpdate("123"))
                .thenReturn(Optional.empty());

        assertThrows(InvalidTransactionException.class, () ->
                transactionService.transfer(request)
        );
    }
}