package com.novobanco.transactional.service;

import com.novobanco.transactional.dto.request.CreateAccountRequest;
import com.novobanco.transactional.dto.response.AccountResponse;
import com.novobanco.transactional.dto.response.MovementResponse;
import com.novobanco.transactional.entity.Cliente;
import com.novobanco.transactional.entity.Cuenta;
import com.novobanco.transactional.entity.Movimiento;
import com.novobanco.transactional.entity.TipoCuenta;
import com.novobanco.transactional.exception.AccountNotFoundException;
import com.novobanco.transactional.repository.ClienteRepository;
import com.novobanco.transactional.repository.CuentaRepository;
import com.novobanco.transactional.repository.MovimientoRepository;
import com.novobanco.transactional.repository.TipoCuentaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TipoCuentaRepository tipoCuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private AccountService accountService;

    // ========================
    // CREATE ACCOUNT
    // ========================

    @Test
    void createAccount_shouldCreateAccount_whenClientExists() {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNombre("Juan");

        TipoCuenta tipoCuenta = new TipoCuenta();
        tipoCuenta.setIdTipoCuenta(1L);
        tipoCuenta.setNombre("ahorros");

        CreateAccountRequest request = new CreateAccountRequest();
        request.setCliente(cliente);
        request.setTipoCuenta(tipoCuenta);
        request.setMoneda("USD");
        request.setEstado(Cuenta.EstadoCuenta.activa);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(tipoCuentaRepository.findById(1L)).thenReturn(Optional.of(tipoCuenta));
        when(cuentaRepository.findByNumeroCuenta(any())).thenReturn(Optional.empty());

        when(cuentaRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertEquals("Juan", response.getNombreCliente());
        assertEquals("ahorros", response.getTipoCuenta());

        verify(cuentaRepository).save(any());
    }

    @Test
    void createAccount_shouldCreateAccount_whenClientIsNew() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Nuevo");

        TipoCuenta tipoCuenta = new TipoCuenta();
        tipoCuenta.setIdTipoCuenta(1L);
        tipoCuenta.setNombre("corriente");

        CreateAccountRequest request = new CreateAccountRequest();
        request.setCliente(cliente);
        request.setTipoCuenta(tipoCuenta);
        request.setMoneda("USD");
        request.setEstado(Cuenta.EstadoCuenta.activa);

        when(clienteRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        when(tipoCuentaRepository.findById(1L)).thenReturn(Optional.of(tipoCuenta));
        when(cuentaRepository.findByNumeroCuenta(any())).thenReturn(Optional.empty());

        when(cuentaRepository.save(any()))
                .thenAnswer(i -> i.getArgument(0));

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        verify(clienteRepository).save(any());
    }

    @Test
    void createAccount_shouldFail_whenTipoCuentaIsNull() {
        CreateAccountRequest request = new CreateAccountRequest();

        assertThrows(IllegalArgumentException.class, () ->
                accountService.createAccount(request)
        );
    }

    // ========================
    // GET ACCOUNT
    // ========================

    @Test
    void getAccountInfo_shouldReturnAccount() {
        Cuenta cuenta = buildCuenta();

        when(cuentaRepository.findById(1L))
                .thenReturn(Optional.of(cuenta));

        AccountResponse response = accountService.getAccountInfo(1L);

        assertEquals("Juan", response.getNombreCliente());
    }

    @Test
    void getAccountInfo_shouldThrow_whenNotFound() {
        when(cuentaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                accountService.getAccountInfo(1L)
        );
    }

    @Test
    void getAccountInfoByNumber_shouldReturnAccount() {
        Cuenta cuenta = buildCuenta();

        when(cuentaRepository.findByNumeroCuenta("123"))
                .thenReturn(Optional.of(cuenta));

        AccountResponse response = accountService.getAccountInfoByNumber("123");

        assertEquals("123", response.getNumeroCuenta());
    }

    // ========================
    // MOVEMENTS
    // ========================

    @Test
    void getAccountMovements_shouldReturnMovements() {
        Cuenta cuenta = buildCuenta();

        Movimiento movimiento = new Movimiento();
        movimiento.setIdMovimiento(1L);
        movimiento.setMonto(new BigDecimal("100"));
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(Movimiento.TipoMovimiento.credito);

        Page<Movimiento> page = new PageImpl<>(List.of(movimiento));

        when(cuentaRepository.findById(1L))
                .thenReturn(Optional.of(cuenta));

        when(movimientoRepository.findByCuentaIdOrderByFechaDesc(eq(1L), any()))
                .thenReturn(page);

        Page<MovementResponse> result =
                accountService.getAccountMovements(1L, Pageable.unpaged());

        assertEquals(1, result.getContent().size());
    }

    @Test
    void getAccountMovements_shouldThrow_whenAccountNotFound() {
        when(cuentaRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () ->
                accountService.getAccountMovements(1L, Pageable.unpaged())
        );
    }

    // ========================
    // HELPER
    // ========================

    private Cuenta buildCuenta() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan");

        TipoCuenta tipoCuenta = new TipoCuenta();
        tipoCuenta.setNombre("ahorros");

        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(1L);
        cuenta.setNumeroCuenta("123");
        cuenta.setSaldoDisponible(new BigDecimal("100"));
        cuenta.setEstado(Cuenta.EstadoCuenta.activa);
        cuenta.setCliente(cliente);
        cuenta.setTipoCuenta(tipoCuenta);

        return cuenta;
    }
}