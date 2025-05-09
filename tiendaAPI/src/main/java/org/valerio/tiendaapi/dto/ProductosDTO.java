package org.valerio.tiendaapi.dto;

public record ProductosDTO(
        String nombre,
        String descripcion,
        Double precio,
        String talla_id,
        String color_id,
        Long stock,
        String genero,
        Integer marcaId,
        Integer categoriaId
) {
}
