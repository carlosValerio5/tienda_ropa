package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Pedidos;


public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {

}
