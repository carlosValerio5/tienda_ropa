package org.valerio.tiendaapi.exceptions;

public class InventarioYaExisteException extends RuntimeException {
    public InventarioYaExisteException(String message) {
        super(message);
    }
}
