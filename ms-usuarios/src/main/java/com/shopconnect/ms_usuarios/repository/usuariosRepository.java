package com.shopconnect.ms_usuarios.repository;

import com.shopconnect.ms_usuarios.model.usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface usuariosRepository extends JpaRepository<usuario, Long> {
    // Hereda automáticamente save(), findById(), findAll(), etc.
}