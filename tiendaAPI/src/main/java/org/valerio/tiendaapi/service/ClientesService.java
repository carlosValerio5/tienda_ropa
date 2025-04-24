package org.valerio.tiendaapi.service;

import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.model.Clientes;
import org.valerio.tiendaapi.repository.ClientesRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ClientesService {
    private final ClientesRepository clientesRepository;

    public ClientesService(ClientesRepository clientesRepository) {
        this.clientesRepository = clientesRepository;
    }

    public List<Clientes> getClientes(){
        return clientesRepository.findAll();
    }

    public List<Clientes> getClientesById(Integer id){
        return clientesRepository.findAll().stream().filter(
                cliente -> id.equals(cliente.getCliente_id())
        ).collect(Collectors.toList());
    }

    public List<Clientes> getClientesByName (String name){
        return clientesRepository.findAll().stream().filter(
                cliente -> name.equals(cliente.getNombre()) || name.equals(cliente.getApellido())
        ).collect(Collectors.toList());
    }

    public Clientes updateCliente(Clientes cliente){
        Clientes clienteToUpdate = clientesRepository.findById(cliente.getCliente_id()).orElse(null);

        if(clienteToUpdate != null){
            clienteToUpdate.setNombre(cliente.getNombre());
            clienteToUpdate.setApellido(cliente.getApellido());
            clienteToUpdate.setDireccion( cliente.getDireccion());
            clienteToUpdate.setTelefono(cliente.getTelefono());
            clienteToUpdate.setEmail(cliente.getEmail());
            clientesRepository.save(clienteToUpdate);
            return clienteToUpdate;
        }
        return null;
    }

    public Clientes addCliente(Clientes cliente){
        if(cliente.getFecha_registro() != null){
            cliente.setFecha_registro(LocalDate.now());
        }
        clientesRepository.save(cliente);
        return cliente;
    }


}
