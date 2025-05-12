package org.valerio.tiendaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.exceptions.CategoriaNotFoundException;
import org.valerio.tiendaapi.exceptions.CategoriaYaExisteException;
import org.valerio.tiendaapi.model.Categorias;
import org.valerio.tiendaapi.service.CategoriasService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@RequestMapping(path="api/v1/categoria")
public class CategoriaController {

    private final CategoriasService categoriasService;

    @Autowired
    public CategoriaController(CategoriasService categoriasService) {
        this.categoriasService = categoriasService;
    }

    @GetMapping
    public List<Categorias> getCategorias(@RequestParam(required = false) Integer id,
                                          @RequestParam(required = false) String nombre) {
        if (id != null) {
            return categoriasService.getCategoriaPorID(id);
        }else if(nombre != null) {
            return categoriasService.getCategoriaPorNombre(nombre);
        }else{
            return categoriasService.getCategorias();
        }
    }

    @PostMapping
    public ResponseEntity<Categorias> addCategoria(@RequestBody Categorias categorias) {
        Categorias addedCategoria;
        try {
            addedCategoria = categoriasService.addCategorias(categorias);
        }catch (CategoriaYaExisteException e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        if (addedCategoria != null) {
            return new ResponseEntity<>(addedCategoria, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{categoriaID}")
    public ResponseEntity<Categorias> deleteCategoria(@PathVariable Integer categoriaID) {
        Categorias deletedCategoria;
        try {
            deletedCategoria = categoriasService.deleteCategorias(categoriaID);
        }catch (CategoriaNotFoundException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (deletedCategoria != null) {
            return new ResponseEntity<>(deletedCategoria, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<Categorias> updateCategoria(@RequestBody Categorias categorias) {
        Categorias updatedCategoria = categoriasService.updateCategorias(categorias);
        if (updatedCategoria != null) {
            return new ResponseEntity<>(updatedCategoria, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
