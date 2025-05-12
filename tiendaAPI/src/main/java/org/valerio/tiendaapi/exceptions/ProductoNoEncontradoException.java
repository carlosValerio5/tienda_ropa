package org.valerio.tiendaapi.exceptions;

public class ProductoNoEncontradoException extends RuntimeException {
    public ProductoNoEncontradoException(String message) {
        super(message);
    }
}
