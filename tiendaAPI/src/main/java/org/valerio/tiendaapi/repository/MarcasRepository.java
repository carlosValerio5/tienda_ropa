package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Marcas;

import java.util.Optional;

public interface MarcasRepository extends JpaRepository<Marcas, Integer> {

    Optional<Marcas> findByNombre(String nombre);
}
