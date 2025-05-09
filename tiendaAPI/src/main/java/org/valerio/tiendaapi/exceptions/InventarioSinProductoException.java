package org.valerio.tiendaapi.exceptions;

public class InventarioSinProductoException extends RuntimeException {
    public InventarioSinProductoException(String message) {
        super(message);
    }
}
