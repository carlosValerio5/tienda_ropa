package org.valerio.tiendaapi.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.dto.ResenaDTO;
import org.valerio.tiendaapi.exceptions.CalificacionNoPermitidaExeption;
import org.valerio.tiendaapi.exceptions.ClienteNoExisteExeption;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.exceptions.ResenaNoEncontradaException;
import org.valerio.tiendaapi.model.Clientes;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.model.Resenas;
import org.valerio.tiendaapi.repository.ClientesRepository;
import org.valerio.tiendaapi.repository.ProductosRepository;
import org.valerio.tiendaapi.repository.ResenasRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        if(!clientesRepository.existsById(resena.getCliente().getClienteId())){
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

    public List<Resenas> getResenas(){
        return resenasRepository.findAll();
    }

    public Resenas crearResena(ResenaDTO resenaDTO) throws ProductoNoEncontradoException, ClienteNoExisteExeption {
        Optional<Productos> producto = productosRepository.findByProductoId(resenaDTO.productoId());
        if(producto.isEmpty()){
            throw new ProductoNoEncontradoException("No se encontro el producto");
        }

        Optional<Clientes> cliente = clientesRepository.findByClienteId(resenaDTO.clienteId());
        if(cliente.isEmpty()){
            throw new ClienteNoExisteExeption("No se encontro el cliente");
        }

        Resenas resena = new Resenas(resenaDTO.calificacion(), resenaDTO.comentario(), LocalDate.now(),
                producto.get(), cliente.get());

        return resenasRepository.save(resena);
    }

    public Resenas updateResenas(ResenaDTO resenaDTO) throws ProductoNoEncontradoException, ClienteNoExisteExeption, ResenaNoEncontradaException {

        Optional<Resenas> resena = resenasRepository.findById(resenaDTO.resenaId());

        if(resena.isEmpty()){
            throw new ResenaNoEncontradaException("La resena no existe");
        }

        Optional<Productos> producto = productosRepository.findByProductoId(resenaDTO.productoId());
        if(producto.isEmpty()&&resenaDTO.productoId()!=null){
            throw new ProductoNoEncontradoException("No se encontro el producto");
        }
        Optional<Clientes> cliente = clientesRepository.findByClienteId(resenaDTO.clienteId());
        if(cliente.isEmpty()&&resenaDTO.clienteId()!=null){
            throw new ClienteNoExisteExeption("No se encontro el cliente");
        }

        if(resenaDTO.comentario()!=null){
            resena.get().setComentario(resenaDTO.comentario());
        }

        if(resenaDTO.calificacion()!=null){
            resena.get().setCalificacion(resenaDTO.calificacion());
        }

        if(resenaDTO.productoId()!=null){
            resena.get().setProducto(producto.get());
        }

        if(resenaDTO.clienteId()!=null){
            resena.get().setCliente(cliente.get());
        }

        return resenasRepository.save(resena.get());

    }

    public Resenas deleteResena(Integer resenaId) throws ProductoNoEncontradoException{
        Optional<Resenas> resena = resenasRepository.findById(resenaId);
        if(resena.isEmpty()){
            throw new ResenaNoEncontradaException("La resena no existe");
        }

        resenasRepository.delete(resena.get());
        return resena.get();
    }
}
