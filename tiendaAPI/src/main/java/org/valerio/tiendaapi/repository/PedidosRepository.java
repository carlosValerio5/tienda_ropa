package org.valerio.tiendaapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.valerio.tiendaapi.model.Clientes;
import org.valerio.tiendaapi.model.Pedidos;

import java.util.List;


public interface PedidosRepository extends JpaRepository<Pedidos, Integer> {

    List<Pedidos> findByCliente(Clientes cliente);

    @Procedure("crear_pedido")
    void crearPedido(@Param("p_cliente_Id") Integer pClienteID,
                     @Param("p_estado") String pEstado,
                     @Param("p_productos")Integer[] pProductos,
                     @Param("p_cantidades") Integer[] pCantidades);

}
