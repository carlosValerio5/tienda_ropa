package org.valerio.tiendaapi.dto;

public record PedidosDTO(
        Integer pClienteId,
        String pEstado,
        Integer[] pProductos,
        Integer[] pCantidades
) {

}
