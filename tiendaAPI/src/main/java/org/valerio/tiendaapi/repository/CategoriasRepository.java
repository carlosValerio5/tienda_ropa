package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Categorias;

public interface CategoriasRepository extends JpaRepository<Categorias, Long> {
}
