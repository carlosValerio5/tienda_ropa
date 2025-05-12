package org.valerio.tiendaapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.exceptions.CalificacionNoPermitidaExeption;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.model.Resenas;
import org.valerio.tiendaapi.service.ResenasService;

import java.util.List;

@RestController
@RequestMapping("api/v1/resenas")
public class ResenasController {
    @Autowired
    private ResenasService resenasService;

    @PostMapping
    public ResponseEntity<Resenas> crearResena(@RequestBody Resenas resena) throws ClienteNoExisteExeption, ProductoNoEncontradoException, CalificacionNoPermitidaExeption {
        Resenas nuevaResena = resenasService.crearResena(resena);
        return new ResponseEntity<>(nuevaResena, HttpStatus.CREATED);
    }

    @GetMapping("/producto/{productoId}")
    public List<Resenas> getResenasByProducto(@PathVariable Integer productoId) {
        return resenasService.getResenasByProductoId(productoId);
    }
}
