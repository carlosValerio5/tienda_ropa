package org.valerio.tiendaapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.valerio.tiendaapi.model.Clientes;
import org.valerio.tiendaapi.service.ClientesService;

import java.util.List;

@RestController
@RequestMapping(path="api/v1/clientes")
public class ClientesController {
    private final ClientesService clientesService;

    @Autowired
    public ClientesController(ClientesService clientesService) {
        this.clientesService = clientesService;
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
}
