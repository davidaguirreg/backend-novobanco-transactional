package com.novobanco.transactional.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovementResponse {

    private Long idMovimiento;
    private BigDecimal monto;
    private LocalDateTime fechaMovimiento;
    private String tipoMovimiento;

    public MovementResponse() {}

    public MovementResponse(Long idMovimiento, BigDecimal monto, LocalDateTime fechaMovimiento, String tipoMovimiento) {
        this.idMovimiento = idMovimiento;
        this.monto = monto;
        this.fechaMovimiento = fechaMovimiento;
        this.tipoMovimiento = tipoMovimiento;
    }

    // Getters and Setters
    public Long getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(Long idMovimiento) { this.idMovimiento = idMovimiento; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

}