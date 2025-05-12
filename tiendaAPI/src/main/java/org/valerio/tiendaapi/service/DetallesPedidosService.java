package org.valerio.tiendaapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.model.DetallesPedido;
import org.valerio.tiendaapi.model.Pedidos;
import org.valerio.tiendaapi.repository.DetallesPedidoRepository;

import java.util.List;

@Service
public class DetallesPedidosService {
    @Autowired
    private DetallesPedidoRepository detallesRepo;


    public DetallesPedido crearDatalle(DetallesPedido detalle){
        return detallesRepo.save(detalle);
    }


    public List<DetallesPedido> getByPedidoId(Integer idPedido){

        Pedidos pedido = new Pedidos(idPedido);

        return detallesRepo.findByPedido(pedido);
    }

}
