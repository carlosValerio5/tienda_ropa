package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Clientes;

import java.util.Optional;

public interface ClientesRepository extends JpaRepository<Clientes, Integer> {

}
