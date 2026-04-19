package com.novobanco.transactional.dto.response;

import java.math.BigDecimal;

public class AccountResponse {

    private Long idCuenta;
    private String numeroCuenta;
    private BigDecimal saldo;
    private String estado;
    private String nombreCliente;
    private String tipoCuenta;

    public AccountResponse() {}

    public AccountResponse(Long idCuenta, String numeroCuenta, BigDecimal saldo, String estado, String nombreCliente, String tipoCuenta) {
        this.idCuenta = idCuenta;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
        this.estado = estado;
        this.nombreCliente = nombreCliente;
        this.tipoCuenta = tipoCuenta;
    }

    // Getters and Setters
    public Long getIdCuenta() { return idCuenta; }
    public void setIdCuenta(Long idCuenta) { this.idCuenta = idCuenta; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
}