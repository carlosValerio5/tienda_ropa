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
                cliente -> id.equals(cliente.getClienteId())
        ).collect(Collectors.toList());
    }

    public List<Clientes> getClientesByName (String name){
        return clientesRepository.findAll().stream().filter(
                cliente -> name.toLowerCase().contains(cliente.getNombre().toLowerCase()) ||
                        name.toLowerCase().contains(cliente.getApellido().toLowerCase())
        ).collect(Collectors.toList());
    }

    public Clientes updateCliente(Clientes cliente){
        Clientes clienteToUpdate = clientesRepository.findById(cliente.getClienteId()).orElse(null);

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
        if(cliente.getFecha_registro() == null){
            cliente.setFecha_registro(LocalDate.now());
        }
        clientesRepository.save(cliente);
        return cliente;
    }

    public Clientes deleteCliente(Integer id){
        Clientes clienteToDelete = clientesRepository.findById(id).orElse(null);
        if(clienteToDelete != null){
            clientesRepository.delete(clienteToDelete);
        }
        return clienteToDelete;
    }

    public List<Clientes> getClientesBySearch(String search){
        List<Clientes> resultado;
        resultado = clientesRepository.findAll().stream().filter(
                clientes -> clientes.getNombre().toLowerCase().contains(search.toLowerCase())
        ).toList();

        if(!resultado.isEmpty()) return resultado;
        resultado = clientesRepository.findAll().stream().filter(
                clientes -> clientes.getApellido().toLowerCase().contains(search.toLowerCase())
        ).toList();

        if(!resultado.isEmpty()) return resultado;
        resultado = clientesRepository.findAll().stream().filter(
                clientes -> clientes.getEmail().toLowerCase().contains(search.toLowerCase())
        ).toList();
        if(!resultado.isEmpty()) return resultado;

        return resultado;
    }

}
