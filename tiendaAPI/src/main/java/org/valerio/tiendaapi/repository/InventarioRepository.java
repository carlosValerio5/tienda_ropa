package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Inventario;
import org.valerio.tiendaapi.model.Productos;

import java.util.List;
import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Integer> {

    Optional<Inventario> findByInventarioId(Integer id);

    List<Inventario> findByProducto(Productos producto);
}
