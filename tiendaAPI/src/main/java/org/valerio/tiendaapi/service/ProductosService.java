package org.valerio.tiendaapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.valerio.tiendaapi.model.Productos;
import org.valerio.tiendaapi.repository.ProductosRepository;

import java.util.Optional;

@Service
public class ProductosService {

    private final ProductosRepository productosRepository;

    @Autowired
    public ProductosService(ProductosRepository productosRepository) {
        this.productosRepository = productosRepository;
    }

    public Productos getById(Integer id) {
        Optional<Productos> producto = productosRepository.findByProductoId(id);

        return producto.orElse(null);

    }


}
