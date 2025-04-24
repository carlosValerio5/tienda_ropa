package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Inventario;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {
}
