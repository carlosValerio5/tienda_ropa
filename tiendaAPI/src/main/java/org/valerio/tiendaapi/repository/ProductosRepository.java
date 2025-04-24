package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Productos;

public interface ProductosRepository extends JpaRepository<Productos, Long> {
}
