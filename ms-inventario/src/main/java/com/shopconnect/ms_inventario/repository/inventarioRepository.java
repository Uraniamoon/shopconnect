package com.shopconnect.ms_inventario.repository;

import com.shopconnect.ms_inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface inventarioRepository extends JpaRepository<Inventario, Long> {
    // ¡Listo! Al heredar de JpaRepository, Spring te regala gratis 
    // los métodos save(), findById(), delete() y findAll().
}