package org.valerio.tiendaapi.exceptions;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(String message) {
        super(message);
    }
}
