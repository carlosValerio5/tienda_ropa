package org.valerio.tiendaapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.dto.PedidosDTO;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.exceptions.PedidoNotFoundException;
import org.valerio.tiendaapi.model.DetallesPedido;
import org.valerio.tiendaapi.model.Pedidos;
import org.valerio.tiendaapi.service.PedidosService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidosController {
    @Autowired
    private PedidosService pedidosService;

    /*
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

     */
    /*
    //Detalles pedido
    @PostMapping("/{pedido_id}/detalles")
    public ResponseEntity<DetallesPedido> agregarDetalle(
            @PathVariable Integer pedido_id,
            @RequestBody DetallesPedido detalle) {

        DetallesPedido nuevoDetalle = pedidosService.agregarDetalleAPedido(pedido_id, detalle);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

     */

    @DeleteMapping("/{pedidoId}")
    public ResponseEntity<Pedidos> eliminarPedido(@PathVariable Integer pedidoId){
        try{
            pedidosService.eliminarPedido(pedidoId);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (PedidoNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/detalles")
    public List<DetallesPedido> obtenerDetalles(@RequestParam(required = true) Integer pedidoId) {
        Pedidos pedido = new Pedidos(pedidoId);
        return pedidosService.obtenerDetallesPorPedido(pedido);
    }

    @PostMapping
    public ResponseEntity<String> crearPedidos(@RequestBody PedidosDTO pedido) {
        try {
            pedidosService.crearPedido(pedido.pClienteId(), pedido.pEstado(), pedido.pProductos(), pedido.pCantidades());
        }catch (RuntimeException e){
            return new ResponseEntity<>("El id del cliente o producto no son validos.", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Pedido Creado", HttpStatus.CREATED);
    }

    @GetMapping
    public List<Pedidos> getPedidos(){
        return pedidosService.getPedidos();
    }

    @GetMapping("/params")
    public List<Pedidos> getPedidos(
            @RequestParam(required = false) String clienteNombre,
            @RequestParam(required = false) String estado
    ){
        return pedidosService.getPedidosByParams(clienteNombre, estado);
    }

}
