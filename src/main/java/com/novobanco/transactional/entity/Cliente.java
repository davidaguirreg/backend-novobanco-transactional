package com.novobanco.transactional.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cuenta> cuentas = new ArrayList<>();

    public Cliente() {}

    public Cliente(Long idCliente, String nombre, List<Cuenta> cuentas) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.cuentas = cuentas;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
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
        private Long idCliente;
        private String nombre;
        private List<Cuenta> cuentas = new ArrayList<>();

        public Builder idCliente(Long idCliente) {
            this.idCliente = idCliente;
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

        public Cliente build() {
            return new Cliente(idCliente, nombre, cuentas);
        }
    }
}