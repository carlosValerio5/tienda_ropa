package org.valerio.tiendaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.exceptions.ProductoYaExisteException;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.repository.ProductosRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductosService {

    private final ProductosRepository productosRepository;

    @Autowired
    public ProductosService(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    public List<Productos> getProductos() {
        return productosRepository.findAll();
    }

    public Productos getById(Integer id) {
        Optional<Productos> producto = productosRepository.findByProductoId(id);

        return producto.orElse(null);

    }

    public List<Productos> getByName(String name) {
        return productosRepository.findByNombreContainingIgnoreCase(name);
    }

    public Productos addProducto(Productos producto) throws ProductoYaExisteException {
        if (productosRepository.findByProductoId(producto.getProductoId()).isPresent()) {
            throw new ProductoYaExisteException("Un producto con este id ya existe.");
        }
        return productosRepository.save(producto);
    }

    public Productos updateProducto(Productos producto) throws ProductoNoEncontradoException {
        Optional<Productos> productoOptional = productosRepository.findByProductoId(producto.getProductoId());

        if(productoOptional.isPresent()) {
            Productos productoToUpdate = productoOptional.get();
            productoToUpdate.setNombre(producto.getNombre());
            productoToUpdate.setPrecio(producto.getPrecio());
            productoToUpdate.setDescripcion(producto.getDescripcion());
            productoToUpdate.setStock(producto.getStock());
            productoToUpdate.setGenero(producto.getGenero());
            return productosRepository.save(productoToUpdate);
        }

        throw new ProductoNoEncontradoException("El producto no se encuentra registrado");

    }

    public Productos deleteProducto(Integer id) throws ProductoNoEncontradoException {
        Optional<Productos> productoOptional = productosRepository.findById(id);
        if(productoOptional.isPresent()) {
            Productos productoToDelete = productoOptional.get();
            productosRepository.delete(productoToDelete);
            return productoToDelete;
        }
        throw new ProductoNoEncontradoException("El producto no existe");
    }


}
