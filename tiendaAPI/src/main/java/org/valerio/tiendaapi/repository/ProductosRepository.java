package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Productos;

import java.util.Optional;

public interface ProductosRepository extends JpaRepository<Productos, Integer> {
    Optional<Productos> findByProductoId(Integer productoId);
}
