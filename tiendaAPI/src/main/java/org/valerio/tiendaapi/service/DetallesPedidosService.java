package org.valerio.tiendaapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.model.DetallesPedido;
import org.valerio.tiendaapi.repository.DetallesPedidoRepository;

@Service
public class DetallesPedidosService {
    @Autowired
    private DetallesPedidoRepository detallesRepo;


    public DetallesPedido crearDatalle(DetallesPedido detalle){
        return detallesRepo.save(detalle);
    }


}
