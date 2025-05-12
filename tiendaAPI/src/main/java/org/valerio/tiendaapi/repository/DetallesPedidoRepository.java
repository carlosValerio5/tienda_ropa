package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.DetallesPedido;
import org.valerio.tiendaapi.model.Pedidos;

import java.util.List;

public interface DetallesPedidoRepository extends JpaRepository<DetallesPedido, Integer> {

    List<DetallesPedido> findByPedido(Pedidos pedido);

}
