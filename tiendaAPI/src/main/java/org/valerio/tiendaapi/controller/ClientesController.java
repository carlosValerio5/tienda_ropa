package org.valerio.tiendaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.model.Clientes;
import org.valerio.tiendaapi.service.ClientesService;

import java.util.List;

@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping(path="api/v1/clientes")
public class ClientesController {
    private final ClientesService clientesService;

    @Autowired
    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
    }

    @GetMapping("/search")
    public List<Clientes> search(@RequestParam String search) {
        return clientesService.getClientesBySearch(search);
    }

    @GetMapping
    public List<Clientes> getClientes(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name
    ){
        if(id != null) {
            return clientesService.getClientesById(id);
        }
        else if(name != null) {
            return clientesService.getClientesByName(name);
        }
        else {
            return clientesService.getClientes();
        }
    }

    @PostMapping
    public ResponseEntity<Clientes> addClientes(@RequestBody Clientes cliente) {
        Clientes createdCliente = clientesService.addCliente(cliente);
        return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
    }

    @DeleteMapping("/{cliente_id}")
    public ResponseEntity<Clientes> deleteClientes(@PathVariable Integer cliente_id) {
        Clientes cliente = clientesService.deleteCliente(cliente_id);
        if (cliente != null) {
            return new ResponseEntity<>(cliente, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    //Tiene que agregar el id en el cuerpo de la request para que funcione
    @PutMapping
    public ResponseEntity<Clientes> updateClientes(@RequestBody Clientes cliente) {

        if(cliente.getClienteId() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Clientes updatedCliente = clientesService.updateCliente(cliente);
        if(updatedCliente != null) {
            return new ResponseEntity<>(updatedCliente, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
