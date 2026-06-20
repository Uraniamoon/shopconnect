package com.shopconnect.ms_pedidos.repository;

import com.shopconnect.ms_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface pedidosRepository extends JpaRepository<Pedido, Long> {
    // Spring Data JPA hereda de forma automática save(), findById(), findAll(), etc.
}
