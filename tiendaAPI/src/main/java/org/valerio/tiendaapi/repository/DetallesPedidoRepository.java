package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.valerio.tiendaapi.model.DetallesPedido;

import java.util.List;

public interface DetallesPedidoRepository extends JpaRepository<DetallesPedido, Integer> {

    List<DetallesPedido> findByPedido(DetallesPedido pedido);
}
