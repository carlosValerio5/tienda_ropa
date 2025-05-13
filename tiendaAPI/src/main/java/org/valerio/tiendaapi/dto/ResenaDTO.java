package org.valerio.tiendaapi.dto;

public record ResenaDTO(
        Integer productoId,
        Integer clienteId,
        Integer calificacion,
        String comentario
) {
}
