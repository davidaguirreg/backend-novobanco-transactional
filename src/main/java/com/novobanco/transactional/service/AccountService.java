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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final TipoCuentaRepository tipoCuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public AccountService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository, TipoCuentaRepository tipoCuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.tipoCuentaRepository = tipoCuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        // Buscar o crear cliente
        Cliente cliente;
        if (request.getCliente() != null &&request.getCliente().getIdCliente() != null) {
            cliente = clienteRepository.findById(request.getCliente().getIdCliente())
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
        } else {
            cliente = clienteRepository.save(request.getCliente());
        }

        // Obtener tipo de cuenta
        TipoCuenta tipoCuenta;
        if(request.getTipoCuenta()!=null && request.getTipoCuenta().getIdTipoCuenta() != null) {
            tipoCuenta = this.tipoCuentaRepository.findById(request.getTipoCuenta().getIdTipoCuenta())
                        .orElseThrow(() -> new IllegalArgumentException("Tipo cuenta no encontrado"));
        } else {
            throw new IllegalArgumentException("Tipo de cuenta es requerido");
        }

        // Generar número de cuenta único
        String numeroCuenta;
        do {
            numeroCuenta = UUID.randomUUID().toString().substring(0, 20).toUpperCase();
        } while (cuentaRepository.findByNumeroCuenta(numeroCuenta).isPresent());

        // Crear cuenta
        Cuenta cuenta = new Cuenta(null, numeroCuenta, BigDecimal.ZERO, request.getMoneda(), request.getEstado(), cliente, tipoCuenta);

        cuenta = cuentaRepository.save(cuenta);

        return mapToAccountResponse(cuenta);
    }

    public AccountResponse getAccountInfo(Long accountId) {
        Cuenta cuenta = cuentaRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
        return mapToAccountResponse(cuenta);
    }

    public AccountResponse getAccountInfoByNumber(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
        return mapToAccountResponse(cuenta);
    }

    private AccountResponse mapToAccountResponse(Cuenta cuenta) {
        return new AccountResponse(cuenta.getIdCuenta(), cuenta.getNumeroCuenta(), cuenta.getSaldoDisponible(), cuenta.getEstado().name(), cuenta.getCliente().getNombre(), cuenta.getTipoCuenta().getNombre());
    }

    public Page<MovementResponse> getAccountMovements(Long accountId, Pageable pageable) {
        // Verificar que la cuenta existe
        cuentaRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));

        Page<Movimiento> movimientos = movimientoRepository.findByCuentaIdOrderByFechaDesc(accountId, pageable);
        return movimientos.map(this::mapToMovementResponse);
    }

    private MovementResponse mapToMovementResponse(Movimiento movimiento) {
        return new MovementResponse(movimiento.getIdMovimiento(), movimiento.getMonto(), movimiento.getFecha(), movimiento.getTipo().name());
    }
        
}