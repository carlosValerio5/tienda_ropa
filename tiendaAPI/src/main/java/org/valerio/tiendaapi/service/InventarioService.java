package org.valerio.tiendaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.exceptions.InventarioNoEncontradoException;
import org.valerio.tiendaapi.exceptions.InventarioSinProductoException;
import org.valerio.tiendaapi.exceptions.InventarioYaExisteException;
import org.valerio.tiendaapi.model.Inventario;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.repository.InventarioRepository;
import org.valerio.tiendaapi.repository.ProductosRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductosRepository productosRepository;

    @Autowired
    public InventarioService(InventarioRepository inventarioRepository, ProductosRepository productosRepository) {
        this.inventarioRepository = inventarioRepository;
        this.productosRepository = productosRepository;
    }

    public Inventario findById(Integer id) {
        return inventarioRepository.findById(id).orElse(null);
    }

    public List<Inventario> findAll() {
        return inventarioRepository.findAll();
    }

    public List<Inventario> findByProducto(Integer productoId) {
        Optional<Productos> producto = productosRepository.findById(productoId);
        if (producto.isPresent()) {
            return inventarioRepository.findByProducto(producto.get());
        }
        return new ArrayList<>();
    }

    public Inventario addInventario(Inventario inventario) throws InventarioYaExisteException {
        if (inventarioRepository.findByInventarioId(inventario.getInventarioId()).isPresent()) {
            throw new InventarioYaExisteException("Ya existe un inventario con ese id");
        }else if(inventario.getProducto()==null) {
            throw new InventarioSinProductoException("El inventario debe incluir un producto");
        }
        return inventarioRepository.save(inventario);
    }

    public Inventario updateInventario(Inventario inventario) throws InventarioNoEncontradoException {
        Optional<Inventario> updatedInventario = inventarioRepository.findByInventarioId(inventario.getInventarioId());
        if (updatedInventario.isPresent()) {
            Inventario inventarioToUpdate = updatedInventario.get();
            inventarioToUpdate.setCantidad(inventario.getCantidad());
            inventarioToUpdate.setFecha_actualizacion(LocalDate.now());
            return inventarioRepository.save(inventarioToUpdate);
        }

        throw new InventarioNoEncontradoException("No existe este inventario");
    }

    public Inventario deleteInventario(Inventario inventario) throws InventarioNoEncontradoException {
        Optional<Inventario> inventarioBusqueda = inventarioRepository.findByInventarioId(inventario.getInventarioId());
        if (inventarioBusqueda.isPresent()) {
            Inventario inventarioToDelete = inventarioBusqueda.get();
            inventarioRepository.delete(inventarioToDelete);
            return inventarioToDelete;
        }
        throw new InventarioNoEncontradoException("No existe este inventario");
    }

}
