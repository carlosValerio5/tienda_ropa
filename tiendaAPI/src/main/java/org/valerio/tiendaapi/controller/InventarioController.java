package org.valerio.tiendaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.dto.InventarioDTO;
import org.valerio.tiendaapi.exceptions.InventarioNoEncontradoException;
import org.valerio.tiendaapi.exceptions.InventarioSinProductoException;
import org.valerio.tiendaapi.exceptions.InventarioYaExisteException;
import org.valerio.tiendaapi.model.Inventario;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.service.InventarioService;
import org.valerio.tiendaapi.service.ProductosService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path="api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;
    private final ProductosService productosService;

    @Autowired
    public InventarioController(InventarioService inventarioService, ProductosService productosService) {
        this.inventarioService = inventarioService;
        this.productosService = productosService;
    }

    @GetMapping
    public List<Inventario> getInventarios(@RequestParam(required=false) Integer id,
                                           @RequestParam(required = false) Integer productoId) {
        if(id != null) {
            return Collections.singletonList(inventarioService.findById(id));
        }else if (productoId != null) {
            return inventarioService.findByProducto(productoId);
        }else {
            return inventarioService.findAll();
        }
    }

    //Se usa un DTO para facilitar la request para el usuario
    //Con el uso del DTO el usuario solo necesita ingresar el id del producto y no la estructura completa
    @PostMapping
    public ResponseEntity<Inventario> createInventario(@RequestBody InventarioDTO inventarioRequest) {

        Productos producto = productosService.getById(inventarioRequest.productoId());
        if(producto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Inventario inventario = new Inventario(inventarioRequest.cantidad(), producto);
        Inventario created;
        try {

            created = inventarioService.addInventario(inventario);
        }catch(InventarioYaExisteException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }catch(InventarioSinProductoException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Inventario> updateInventario(@RequestBody InventarioDTO inventarioRequest) {

        Productos producto = productosService.getById(inventarioRequest.productoId());

        if(producto == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(inventarioRequest.id() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Inventario inventario = new Inventario(inventarioRequest.id(),inventarioRequest.cantidad(), producto);
        Inventario updated;

        try{
            updated = inventarioService.updateInventario(inventario);
        }catch(InventarioNoEncontradoException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);

    }
}

