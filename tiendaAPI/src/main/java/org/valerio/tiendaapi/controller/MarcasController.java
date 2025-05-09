package org.valerio.tiendaapi.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.model.Marcas;
import org.valerio.tiendaapi.service.MarcasService;



import java.util.List;

@RestController
@RequestMapping(path="api/v1/categoria")
public class MarcasController {
    @Autowired
    private MarcasService marcasService;

    @GetMapping
    public List<Marcas> getAllMarcas() {
        return marcasService.getAllMarcas();
    }

    @GetMapping("/{marca_id}")
    public ResponseEntity<Marcas> getMarcaById(@PathVariable Integer marca_id) {
        Marcas marca = marcasService.getMarcaById(marca_id);
        return ResponseEntity.ok(marca); // Devuelve 200 OK
    }

    // POST /api/marcas → Crear nueva marca
    @PostMapping
    public ResponseEntity<Marcas> createMarca(@RequestBody Marcas marca) {
        Marcas nuevaMarca = marcasService.createMarca(marca);
        return new ResponseEntity<>(nuevaMarca, HttpStatus.CREATED); // 201 Created
    }

    // PUT /api/marcas/{id} → Actualizar marca
    @PutMapping("/{marca_id}")
    public ResponseEntity<Marcas> updateMarca(@PathVariable Integer marca_id, @RequestBody Marcas marca) {
        Marcas marcaActualizada = marcasService.updateMarca(marca_id, marca);
        return ResponseEntity.ok(marcaActualizada);
    }

    // DELETE /api/marcas/{id} → Eliminar marca
    @DeleteMapping("/{marca_id}")
    public ResponseEntity<Void> deleteMarca(@PathVariable Integer marca_id) {
        marcasService.deleteMarca(marca_id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
