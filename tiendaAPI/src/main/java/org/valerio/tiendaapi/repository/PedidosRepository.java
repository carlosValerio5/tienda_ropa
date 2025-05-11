package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Clientes;
import org.valerio.tiendaapi.model.Pedidos;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {
    List<Pedidos>findByCliente(Clientes cliente);
}
