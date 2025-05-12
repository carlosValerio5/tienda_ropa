package org.valerio.tiendaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.exceptions.ProductoNoEncontradoException;
import org.valerio.tiendaapi.exceptions.ProductoYaExisteException;
import org.valerio.tiendaapi.model.Categorias;
import org.valerio.tiendaapi.model.Marcas;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.repository.CategoriasRepository;
import org.valerio.tiendaapi.repository.MarcasRepository;
import org.valerio.tiendaapi.repository.ProductosRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProductosService {

    private final ProductosRepository productosRepository;
    private final MarcasRepository marcasRepository;
    private final CategoriasRepository categoriasRepository;

    @Autowired
    public ProductosService(ProductosRepository productosRepository, MarcasRepository marcasRepository, CategoriasRepository categoriasRepository) {
        this.productosRepository = productosRepository;
        this.marcasRepository = marcasRepository;
        this.categoriasRepository = categoriasRepository;
    }

    public List<Productos> getProductos() {
        List<Productos> productos =  productosRepository.findAll();
        for(Productos producto: productos) {
            if(producto.getCategorias()!=null) {
                producto.setCategoriaNombre(producto.getCategorias().getNombre());
            }
            if (producto.getMarca()!=null) {
                producto.setMarcaNombre(producto.getMarca().getNombre());
            }
        }
        return productos;
    }

    public List<Productos> getByParams(String nombre, String talla, String color, String marca) {
        List<Productos> productos =  productosRepository.findAll();
        if(!nombre.isEmpty()) {
            productos = productos.stream().filter(
                    Objects::nonNull
            ).filter(
                    producto -> {
                        String productoNombre = producto.getNombre();
                        return !productoNombre.isEmpty() && productoNombre.toLowerCase().contains(nombre.toLowerCase());
                    }
            ).toList();
        }
        if(!talla.isEmpty()) {
            productos = productos.stream().filter(
                    Objects::nonNull
            ).filter(
                    producto -> {
                        String tallaResult = producto.getTallaId();
                        return tallaResult != null && tallaResult.toLowerCase().contains(talla.toLowerCase());
                    }
            ).toList();
        }
        if(!color.isEmpty()) {
            productos = productos.stream().filter(
                    Objects::nonNull
            ).filter(
                    producto -> {
                        String colorResult = producto.getColor_id();
                        return colorResult != null && colorResult.toLowerCase().contains(color.toLowerCase());
                    }
            ).toList();
        }
        if(!marca.isEmpty()) {
            productos = productos.stream().filter(
                    Objects::nonNull
            ).filter(
                    producto -> {
                        Marcas marcaObj = producto.getMarca();
                        return marcaObj != null && marcaObj.getNombre().toLowerCase().contains(marca.toLowerCase());
                    }
            ).toList();
        }
        return productos;
    }

    public Productos getById(Integer id) {
        Optional<Productos> producto = productosRepository.findByProductoId(id);

        return producto.orElse(null);

    }

    public List<Productos> getByTalla(String talla){
        return productosRepository.findByTallaIdIgnoreCase(talla);
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
            productoToUpdate.setColor_id(producto.getColor_id());
            if(producto.getMarcaNombre()!=null) {
                Optional<Marcas> marca = marcasRepository.findByNombre(producto.getMarcaNombre());
                marca.ifPresent(productoToUpdate::setMarca);
            }
            if(producto.getCategoriaNombre()!=null) {
                Optional<Categorias> categorias = categoriasRepository.findByNombre(producto.getCategoriaNombre());
                categorias.ifPresent(productoToUpdate::setCategorias);
            }

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
