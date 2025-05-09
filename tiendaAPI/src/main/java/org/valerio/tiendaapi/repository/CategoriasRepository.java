package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Categorias;

import java.util.Optional;

public interface CategoriasRepository extends JpaRepository<Categorias, Long> {

    Optional<Categorias> findByCategoriaId(Integer id);

}
