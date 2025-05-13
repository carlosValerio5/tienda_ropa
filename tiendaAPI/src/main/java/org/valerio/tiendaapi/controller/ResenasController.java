package org.valerio.tiendaapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.dto.ResenaDTO;
import org.valerio.tiendaapi.exceptions.CalificacionNoPermitidaExeption;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.exceptions.ResenaNoEncontradaException;
import org.valerio.tiendaapi.model.Resenas;
import org.valerio.tiendaapi.service.ResenasService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
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

    @PostMapping("/add")
    public ResponseEntity<Resenas> crearResena(@RequestBody ResenaDTO resenaDTO){

        Resenas resena;

        try{
            resena = resenasService.crearResena(resenaDTO);
        }catch (ClienteNoExisteExeption | ProductoNoEncontradoException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(resena, HttpStatus.CREATED);
    }

    @GetMapping("/producto/{productoId}")
    public List<Resenas> getResenasByProducto(@PathVariable Integer productoId) {
        return resenasService.getResenasByProductoId(productoId);
    }

    @GetMapping
    public List<Resenas> getResenas() {
        return resenasService.getResenas();
    }

    @PutMapping
    public ResponseEntity<Resenas> updateResenas(@RequestBody ResenaDTO resenaDTO){
        Resenas resena;
        try{
            resena = resenasService.updateResenas(resenaDTO);
        }catch (ClienteNoExisteExeption | ProductoNoEncontradoException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (ResenaNoEncontradaException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(resena, HttpStatus.OK);
    }
}
