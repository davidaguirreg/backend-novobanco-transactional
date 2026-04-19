package com.novobanco.transactional.dto.request;

import com.novobanco.transactional.entity.Cliente;
import com.novobanco.transactional.entity.Cuenta;
import com.novobanco.transactional.entity.TipoCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateAccountRequest {

    @NotNull(message = "El cliente es obligatorio")
    private Cliente cliente;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta;

    @NotBlank(message = "La moneda es obligatoria")
    private String moneda;

    @NotNull(message = "El estado de la cuenta es obligatorio")
    private Cuenta.EstadoCuenta estado;

    public CreateAccountRequest() {}

    public CreateAccountRequest(Cliente cliente, TipoCuenta tipoCuenta, String moneda, Cuenta.EstadoCuenta estado) {
        this.cliente = cliente;
        this.tipoCuenta = tipoCuenta;
        this.moneda = moneda;
        this.estado = estado;
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public TipoCuenta getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(TipoCuenta tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }

    public Cuenta.EstadoCuenta getEstado() { return estado; }
    public void setEstado(Cuenta.EstadoCuenta estado) { this.estado = estado; }
}