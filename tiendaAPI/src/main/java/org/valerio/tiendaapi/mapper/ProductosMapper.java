package org.valerio.tiendaapi.mapper;

import org.valerio.tiendaapi.dto.ProductosDTO;
import org.valerio.tiendaapi.model.Productos;

import java.util.function.Function;

public class ProductosMapper implements Function<ProductosDTO, Productos> {

    @Override
    public Productos apply(ProductosDTO productosDTO) {
        Productos productos = new Productos();
        productos.setNombre(productosDTO.nombre());
        productos.setPrecio(productosDTO.precio());
        productos.setDescripcion(productosDTO.descripcion());
        productos.setTallaId(productosDTO.talla_id());
        productos.setColor_id(productosDTO.color_id());
        productos.setStock(productosDTO.stock());
        productos.setGenero(productosDTO.genero());
        return productos;
    }

}
