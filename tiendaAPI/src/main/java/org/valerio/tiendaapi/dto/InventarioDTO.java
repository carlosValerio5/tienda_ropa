package org.valerio.tiendaapi.dto;

import java.time.LocalDate;

/**
 *
 * @param productoId
 * @param cantidad
 *
 *
 * Record para permitir que el ususario agregue un inventario sin la necesidad
 * de especificar una instancia de la clase Producto,
 * tan solo necesita especificar el id de un producto existente.
 */
public record InventarioDTO(
        Integer id,
        Integer productoId,
        Long cantidad
) {
}
