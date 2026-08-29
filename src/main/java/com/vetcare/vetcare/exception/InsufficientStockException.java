package com.vetcare.vetcare.exception;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(String mensaje) {
        super(mensaje);
    }
}
