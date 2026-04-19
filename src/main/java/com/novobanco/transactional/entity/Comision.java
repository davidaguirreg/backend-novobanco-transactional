package com.novobanco.transactional.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "comision")
public class Comision {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comision")
    private Long idComision;

    @Column(name = "tipo_transaccion", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;

    @Column(name = "monto_fijo", precision = 10, scale = 2)
    private BigDecimal montoFijo;

    @Column(name = "porcentaje", precision = 5, scale = 2)
    private BigDecimal porcentaje;

    @Column(name = "moneda", length = 10)
    private String moneda;

    @Column(name = "activa", nullable = false)
    private Boolean activa = true;

    public Comision() {}

    public Comision(Long idComision, TipoTransaccion tipoTransaccion, BigDecimal montoFijo, BigDecimal porcentaje, String moneda, Boolean activa) {
        this.idComision = idComision;
        this.tipoTransaccion = tipoTransaccion;
        this.montoFijo = montoFijo;
        this.porcentaje = porcentaje;
        this.moneda = moneda;
        this.activa = activa != null ? activa : true;
    }

    // Getters and Setters
    public Long getIdComision() { return idComision; }
    public void setIdComision(Long idComision) { this.idComision = idComision; }

    public TipoTransaccion getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(TipoTransaccion tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public BigDecimal getMontoFijo() { return montoFijo; }
    public void setMontoFijo(BigDecimal montoFijo) { this.montoFijo = montoFijo; }

    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }

    public enum TipoTransaccion {
        RETIRO, TRANSFERENCIA
    }
}