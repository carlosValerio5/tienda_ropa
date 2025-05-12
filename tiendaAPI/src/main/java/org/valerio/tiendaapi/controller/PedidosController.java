package org.valerio.tiendaapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.model.DetallesPedido;
import org.valerio.tiendaapi.model.Pedidos;
import org.valerio.tiendaapi.service.PedidosService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidosController {
    @Autowired
    private PedidosService pedidosService;
    @PostMapping
    public ResponseEntity<Pedidos> crearPedido(@RequestBody Pedidos pedido) throws ClienteNoExisteExeption {
        try {
            Pedidos nuevoPedido = pedidosService.crearPedido(pedido);
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
        }catch (ClienteNoExisteExeption e)
        {
            System.out.println(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);

    }
//Detalles pedido
    @PostMapping("/{pedido_id}/detalles")
    public ResponseEntity<DetallesPedido> agregarDetalle(
            @PathVariable Integer pedido_id,
            @RequestBody DetallesPedido detalle) {

        DetallesPedido nuevoDetalle = pedidosService.agregarDetalleAPedido(pedido_id, detalle);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @GetMapping("/{pedido}/detalles")
    public List<DetallesPedido> obtenerDetalles(@PathVariable DetallesPedido pedido) {
        return pedidosService.obtenerDetallesPorPedido(pedido);
    }

}
