package com.shopconnect.ms_productos.repository;

import com.shopconnect.ms_productos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface productosRepository extends JpaRepository<Producto, Long> {
    // Hereda de forma automática save(), findById(), findAll(), etc.
}