package com.novobanco.transactional.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaccion")
public class Transaccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Long idTransaccion;

    @Column(name = "tipo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;

    @Column(name = "fecha_hora_registro", nullable = false)
    private LocalDateTime fechaTransaccion;

    @Column(name = "referencia_unica", nullable = false, unique = true, length = 100)
    private String referenciaUnica;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoTransaccion estado;

    @OneToMany(mappedBy = "transaccion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movimiento> movimientos = new ArrayList<>();

    public Transaccion() {}

    public Transaccion(Long idTransaccion, TipoTransaccion tipoTransaccion, LocalDateTime fechaTransaccion, String referenciaUnica, EstadoTransaccion estado, List<Movimiento> movimientos) {
        this.idTransaccion = idTransaccion;
        this.tipoTransaccion = tipoTransaccion;
        this.fechaTransaccion = fechaTransaccion;
        this.referenciaUnica = referenciaUnica;
        this.estado = estado;
        this.movimientos = movimientos != null ? movimientos : new ArrayList<>();
    }

    // Getters and Setters
    public Long getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(Long idTransaccion) { this.idTransaccion = idTransaccion; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }

    public TipoTransaccion getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public String getReferenciaUnica() { return referenciaUnica; }
    public void setReferenciaUnica(String referenciaUnica) { this.referenciaUnica = referenciaUnica; }

    public EstadoTransaccion getEstado() { return estado; }
    public void setEstado(EstadoTransaccion estado) { this.estado = estado; }

    public List<Movimiento> getMovimientos() { return movimientos; }
    public void setMovimientos(List<Movimiento> movimientos) { this.movimientos = movimientos; }

    public enum TipoTransaccion {
        deposito, retiro, transferencia
    }

    public enum EstadoTransaccion {
        exitosa, fallida, revertida
    }
}