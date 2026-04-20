package com.novobanco.transactional.service;

import com.novobanco.transactional.dto.request.DepositRequest;
import com.novobanco.transactional.dto.request.TransferRequest;
import com.novobanco.transactional.dto.request.WithdrawalRequest;
import com.novobanco.transactional.dto.response.TransactionResponse;
import com.novobanco.transactional.entity.*;
import com.novobanco.transactional.exception.InsufficientFundsException;
import com.novobanco.transactional.exception.InvalidTransactionException;
import com.novobanco.transactional.repository.CuentaRepository;
import com.novobanco.transactional.repository.MovimientoRepository;
import com.novobanco.transactional.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class TransactionService {

    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;
    private final MovimientoRepository movimientoRepository;

    public TransactionService(CuentaRepository cuentaRepository, TransaccionRepository transaccionRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuentaForUpdate(request.getNumeroCuenta())
                .orElseThrow(() -> new InvalidTransactionException("Cuenta no encontrada"));

        if (cuenta.getEstado() != Cuenta.EstadoCuenta.activa) {
            throw new InvalidTransactionException("La cuenta no está activa");
        }

        // Crear transacción
        Transaccion transaccion = new Transaccion(null, Transaccion.TipoTransaccion.deposito, LocalDateTime.now(), UUID.randomUUID().toString(), Transaccion.EstadoTransaccion.exitosa, new ArrayList<>());
        transaccion = transaccionRepository.save(transaccion);

        // Actualizar saldo
        BigDecimal saldoAnterior = cuenta.getSaldoDisponible();
        cuenta.setSaldoDisponible(saldoAnterior.add(request.getMonto()));
        cuentaRepository.save(cuenta);

        // Crear movimiento
        Movimiento movimiento = new Movimiento(null, request.getMonto(), LocalDateTime.now(), Movimiento.TipoMovimiento.credito, Movimiento.TipoEstado.activo, cuenta, transaccion);
        transaccion.getMovimientos().add(movimiento);

        movimientoRepository.save(movimiento);

        return mapToTransactionResponse(transaccion);
    }

    @Transactional
    public TransactionResponse withdraw(WithdrawalRequest request) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuentaForUpdate(request.getNumeroCuenta())
                .orElseThrow(() -> new InvalidTransactionException("Cuenta no encontrada"));

        if (cuenta.getEstado() != Cuenta.EstadoCuenta.activa) {
            throw new InvalidTransactionException("La cuenta no está activa");
        }

        if (cuenta.getSaldoDisponible().compareTo(request.getMonto()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente");
        }

        // Crear transacción
        Transaccion transaccion = new Transaccion(null, Transaccion.TipoTransaccion.retiro, LocalDateTime.now(), UUID.randomUUID().toString(), Transaccion.EstadoTransaccion.exitosa, new ArrayList<>());
        transaccion = transaccionRepository.save(transaccion);

        // Actualizar saldo
        BigDecimal saldoAnterior = cuenta.getSaldoDisponible();
        cuenta.setSaldoDisponible(saldoAnterior.subtract(request.getMonto()));
        cuentaRepository.save(cuenta);

        // Crear movimiento
        Movimiento movimiento = new Movimiento(null, request.getMonto(), LocalDateTime.now(), Movimiento.TipoMovimiento.debito, Movimiento.TipoEstado.activo, cuenta, transaccion);
        transaccion.getMovimientos().add(movimiento);

        movimientoRepository.save(movimiento);

        return mapToTransactionResponse(transaccion);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getNumeroCuentaOrigen().equals(request.getNumeroCuentaDestino())) {
            throw new InvalidTransactionException("No se puede transferir a la misma cuenta");
        }

        Cuenta cuentaOrigen = cuentaRepository.findByNumeroCuentaForUpdate(request.getNumeroCuentaOrigen())
                .orElseThrow(() -> new InvalidTransactionException("Cuenta origen no encontrada"));

        Cuenta cuentaDestino = cuentaRepository.findByNumeroCuentaForUpdate(request.getNumeroCuentaDestino())
                .orElseThrow(() -> new InvalidTransactionException("Cuenta destino no encontrada"));

        if (cuentaOrigen.getEstado() != Cuenta.EstadoCuenta.activa) {
            throw new InvalidTransactionException("La cuenta origen no está activa");
        }

        if (cuentaDestino.getEstado() != Cuenta.EstadoCuenta.activa) {
            throw new InvalidTransactionException("La cuenta destino no está activa");
        }

        if (cuentaOrigen.getSaldoDisponible().compareTo(request.getMonto()) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente en cuenta origen");
        }

        // Crear transacción
        Transaccion transaccion = new Transaccion(null, Transaccion.TipoTransaccion.transferencia, LocalDateTime.now(), UUID.randomUUID().toString(), Transaccion.EstadoTransaccion.exitosa, new ArrayList<>());
        transaccion = transaccionRepository.save(transaccion);

        // Actualizar saldos
        BigDecimal saldoOrigenAnterior = cuentaOrigen.getSaldoDisponible();
        cuentaOrigen.setSaldoDisponible(saldoOrigenAnterior.subtract(request.getMonto()));
        cuentaRepository.save(cuentaOrigen);

        BigDecimal saldoDestinoAnterior = cuentaDestino.getSaldoDisponible();
        cuentaDestino.setSaldoDisponible(saldoDestinoAnterior.add(request.getMonto()));
        cuentaRepository.save(cuentaDestino);

        // Movimiento para cuenta origen
        Movimiento movimientoOrigen = new Movimiento(null, request.getMonto(), LocalDateTime.now(), Movimiento.TipoMovimiento.debito, Movimiento.TipoEstado.activo, cuentaOrigen, transaccion);
        transaccion.getMovimientos().add(movimientoOrigen);
        movimientoRepository.save(movimientoOrigen);

        // Movimiento para cuenta destino
        Movimiento movimientoDestino = new Movimiento(null, request.getMonto(), LocalDateTime.now(), Movimiento.TipoMovimiento.credito, Movimiento.TipoEstado.activo, cuentaDestino, transaccion);
        transaccion.getMovimientos().add(movimientoDestino);
        movimientoRepository.save(movimientoDestino);

        return mapToTransactionResponse(transaccion);
    }

    private TransactionResponse mapToTransactionResponse(Transaccion transaccion) {
        BigDecimal monto = transaccion.getMovimientos().stream()
                .findFirst()
                .map(Movimiento::getMonto)
                .orElse(BigDecimal.ZERO);

        String numeroCuentaOrigen = transaccion.getMovimientos().stream()
                .filter(m -> m.getTipo() == Movimiento.TipoMovimiento.debito)
                .map(m -> m.getCuenta().getNumeroCuenta())
                .findFirst()
                .orElse(null);

        String numeroCuentaDestino = transaccion.getMovimientos().stream()
                .filter(m -> m.getTipo() == Movimiento.TipoMovimiento.credito)
                .map(m -> m.getCuenta().getNumeroCuenta())
                .findFirst()
                .orElse(null);

        return new TransactionResponse(
                transaccion.getReferenciaUnica(),
                monto,
                transaccion.getFechaTransaccion(),
                transaccion.getTipoTransaccion().name(),
                numeroCuentaOrigen,
                numeroCuentaDestino
        );
    }
}