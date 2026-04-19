package com.novobanco.transactional.repository;

import com.novobanco.transactional.entity.Comision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComisionRepository extends JpaRepository<Comision, Long> {
}