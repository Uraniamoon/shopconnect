package com.shopconnect.ms_pagos.repository;

import com.shopconnect.ms_pagos.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface pagosRepository extends JpaRepository<Pago, Long> {
    // Hereda automáticamente save(), findById(), findAll(), etc.
}