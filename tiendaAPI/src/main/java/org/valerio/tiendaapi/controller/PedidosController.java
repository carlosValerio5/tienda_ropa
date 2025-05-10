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
@RequestMapping("/api/pedidos")
public class PedidosController {
    @Autowired
    private PedidosService pedidosService;
    @PostMapping
    public ResponseEntity<Pedidos> crearPedido(@RequestBody Pedidos pedido) throws ClienteNoExisteExeption {
        Pedidos nuevoPedido = pedidosService.crearPedido(pedido);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }
//Detalles pedido
    @PostMapping("/{pedidoId}/detalles")
    public ResponseEntity<DetallesPedido> agregarDetalle(
            @PathVariable Integer pedidoId,
            @RequestBody DetallesPedido detalle) {

        DetallesPedido nuevoDetalle = pedidosService.agregarDetalleAPedido(pedidoId, detalle);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    // 👇 Endpoint para listar detalles de un pedido
    @GetMapping("/{pedidoId}/detalles")
    public List<DetallesPedido> obtenerDetalles(@PathVariable Integer pedidoId) {
        return pedidosService.obtenerDetallesPorPedido(pedidoId);
    }

}
