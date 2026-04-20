package com.novobanco.transactional.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private String referenciaTransaccion;
    private BigDecimal monto;
    private LocalDateTime fechaTransaccion;
    private String tipoTransaccion;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;

    public TransactionResponse() {}

    public TransactionResponse(String referenciaTransaccion, BigDecimal monto, LocalDateTime fechaTransaccion, String tipoTransaccion, String numeroCuentaOrigen, String numeroCuentaDestino) {
        this.referenciaTransaccion = referenciaTransaccion;
        this.monto = monto;
        this.fechaTransaccion = fechaTransaccion;
        this.tipoTransaccion = tipoTransaccion;
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.numeroCuentaDestino = numeroCuentaDestino;
    }

    // Getters and Setters
    public String getReferenciaTransaccion() { return referenciaTransaccion; }
    public void setReferenciaTransaccion(String referenciaTransaccion) { this.referenciaTransaccion = referenciaTransaccion; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }
    public void setFechaTransaccion(LocalDateTime fechaTransaccion) { this.fechaTransaccion = fechaTransaccion; }

    public String getTipoTransaccion() { return tipoTransaccion; }
    public void setTipoTransaccion(String tipoTransaccion) { this.tipoTransaccion = tipoTransaccion; }

    public String getNumeroCuentaOrigen() { return numeroCuentaOrigen; }
    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) { this.numeroCuentaOrigen = numeroCuentaOrigen; }

    public String getNumeroCuentaDestino() { return numeroCuentaDestino; }
    public void setNumeroCuentaDestino(String numeroCuentaDestino) { this.numeroCuentaDestino = numeroCuentaDestino; }
}