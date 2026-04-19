package com.novobanco.transactional.repository;

import com.novobanco.transactional.entity.Cuenta;
import com.novobanco.transactional.entity.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.idCuenta = :cuentaId ORDER BY m.fechaMovimiento DESC")
    Page<Movimiento> findMovimientosByCuentaId(@Param("cuentaId") Long cuentaId, Pageable pageable);
}