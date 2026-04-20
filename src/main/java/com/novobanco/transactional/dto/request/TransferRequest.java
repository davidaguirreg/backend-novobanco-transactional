package com.novobanco.transactional.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TransferRequest {

    @NotBlank(message = "El numero de cuenta origen es obligatorio")
    private String numeroCuentaOrigen;

    @NotBlank(message = "El numero de cuenta destino es obligatorio")
    private String numeroCuentaDestino;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    public TransferRequest() {}

    public TransferRequest(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.numeroCuentaDestino = numeroCuentaDestino;
        this.monto = monto;
    }

    public String getNumeroCuentaOrigen() { return numeroCuentaOrigen; }
    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) { this.numeroCuentaOrigen = numeroCuentaOrigen; }

    public String getNumeroCuentaDestino() { return numeroCuentaDestino; }
    public void setNumeroCuentaDestino(String numeroCuentaDestino) { this.numeroCuentaDestino = numeroCuentaDestino; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}