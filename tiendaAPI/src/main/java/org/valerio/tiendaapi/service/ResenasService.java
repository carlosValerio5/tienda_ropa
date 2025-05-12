package org.valerio.tiendaapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.exceptions.CalificacionNoPermitidaExeption;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.model.Resenas;
import org.valerio.tiendaapi.repository.ClientesRepository;
import org.valerio.tiendaapi.repository.ProductosRepository;
import org.valerio.tiendaapi.repository.ResenasRepository;

import java.util.List;

@Service
public class ResenasService {
    @Autowired
    private ResenasRepository resenasRepository;
    @Autowired
    private ProductosRepository productosRepository;
    @Autowired
    private ClientesRepository clientesRepository;

    public Resenas crearResena(Resenas resena) throws ProductoNoEncontradoException, ClienteNoExisteExeption, CalificacionNoPermitidaExeption {
        if(!productosRepository.existsById(resena.getProducto().getProductoId())){
            throw new ProductoNoEncontradoException("No se encontro el producto");
        }
        if(!clientesRepository.existsById(resena.getCliente().getCliente_id())){
            throw new ClienteNoExisteExeption("No se encontro el cliente");
        }
        if(resena.getCalificacion()<1 || resena.getCalificacion()>5){
            throw new CalificacionNoPermitidaExeption("La calificacion debe ser entre 1 y 5");
        }
        return resenasRepository.save(resena);

    }

    public List<Resenas> getResenasByProductoId(Integer productoId){
        return resenasRepository.findByProductoProductoId(productoId);
    }
}
