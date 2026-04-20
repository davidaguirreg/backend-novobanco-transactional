package com.novobanco.transactional.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DepositRequest {

    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String numeroCuenta;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    public DepositRequest() {}

    public DepositRequest(String numeroCuenta, BigDecimal monto) {
        this.numeroCuenta = numeroCuenta;
        this.monto = monto;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}