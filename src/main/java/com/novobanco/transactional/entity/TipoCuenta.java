package com.novobanco.transactional.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipo_cuenta")
public class TipoCuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_cuenta")
    private Long idTipoCuenta;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "tipoCuenta", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuenta> cuentas = new ArrayList<>();

    public TipoCuenta() {}

    public TipoCuenta(Long idTipoCuenta, String nombre, List<Cuenta> cuentas) {
        this.idTipoCuenta = idTipoCuenta;
        this.nombre = nombre;
        this.cuentas = cuentas;
    }

    public Long getIdTipoCuenta() {
        return idTipoCuenta;
    }

    public void setIdTipoCuenta(Long idTipoCuenta) {
        this.idTipoCuenta = idTipoCuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long idTipoCuenta;
        private String nombre;
        private List<Cuenta> cuentas = new ArrayList<>();

        public Builder idTipoCuenta(Long idTipoCuenta) {
            this.idTipoCuenta = idTipoCuenta;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder cuentas(List<Cuenta> cuentas) {
            this.cuentas = cuentas;
            return this;
        }

        public TipoCuenta build() {
            return new TipoCuenta(idTipoCuenta, nombre, cuentas);
        }
    }
}