package com.novobanco.transactional.repository;

import com.novobanco.transactional.entity.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    @Query("SELECT m FROM Movimiento m WHERE m.cuenta.idCuenta = :cuentaId ORDER BY m.fechaMovimiento DESC")
    Page<Movimiento> findByCuentaIdOrderByFechaMovimientoDesc(@Param("cuentaId") Long cuentaId, Pageable pageable);
}