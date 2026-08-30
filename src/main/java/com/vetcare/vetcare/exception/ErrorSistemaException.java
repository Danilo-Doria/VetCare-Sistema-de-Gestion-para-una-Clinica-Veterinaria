package com.vetcare.vetcare.exception;

public class ErrorSistemaException extends BusinessException {

    public ErrorSistemaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
