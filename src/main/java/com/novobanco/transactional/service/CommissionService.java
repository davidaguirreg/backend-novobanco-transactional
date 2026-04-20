package com.novobanco.transactional.service;

import com.novobanco.transactional.entity.Comision;
import com.novobanco.transactional.repository.ComisionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CommissionService {

    private final ComisionRepository comisionRepository;

    public CommissionService(ComisionRepository comisionRepository) {
        this.comisionRepository = comisionRepository;
    }

    public BigDecimal calculateCommission(String tipoTransaccion, BigDecimal monto) {
        List<Comision> comisiones = comisionRepository.findAll();
        // Lógica simple: buscar comisión activa por tipo
        return comisiones.stream()
                .filter(c -> c.getTipoTransaccion().name().equalsIgnoreCase(tipoTransaccion) && c.getActiva())
                .findFirst()
                .map(c -> {
                    if (c.getMontoFijo() != null) {
                        return c.getMontoFijo();
                    } else if (c.getPorcentaje() != null) {
                        return monto.multiply(c.getPorcentaje().divide(BigDecimal.valueOf(100)));
                    }
                    return BigDecimal.ZERO;
                })
                .orElse(BigDecimal.ZERO);
    }
}