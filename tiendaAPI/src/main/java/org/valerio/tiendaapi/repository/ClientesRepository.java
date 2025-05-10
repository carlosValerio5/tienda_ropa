package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Clientes;

public interface ClientesRepository extends JpaRepository<Clientes, Integer> {

}
