package com.novobanco.transactional.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long idMovimiento;

    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEstado estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_transaccion")
    private Transaccion transaccion;

    public Movimiento() {}

    public Movimiento(Long idMovimiento, BigDecimal monto, LocalDateTime fecha, TipoMovimiento tipo, TipoEstado estado, Cuenta cuenta, Transaccion transaccion) {
        this.idMovimiento = idMovimiento;
        this.monto = monto;
        this.fecha = fecha;
        this.tipo = tipo;
        this.estado = estado;
        this.cuenta = cuenta;
        this.transaccion = transaccion;
    }

    // Getters and Setters
    public Long getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Long idMovimiento) { this.idMovimiento = idMovimiento; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public TipoMovimiento getTipo() { return tipo; }
    public void setTipo(TipoMovimiento tipo) { this.tipo = tipo; }

    public TipoEstado getEstado() { return estado; }
    public void setEstado(TipoEstado estado) { this.estado = estado; }

    public Cuenta getCuenta() { return cuenta; }
    public void setCuenta(Cuenta cuenta) { this.cuenta = cuenta; }

    public Transaccion getTransaccion() { return transaccion; }
    public void setTransaccion(Transaccion transaccion) { this.transaccion = transaccion; }

    public enum TipoMovimiento {
        debito, credito
    }

    public enum TipoEstado {
        activo, revertido
    }
}