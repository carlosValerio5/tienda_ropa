package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.Pedidos;

import java.util.List;

public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {
    List<Pedidos>findByClienteId(Integer clienteId);
}
