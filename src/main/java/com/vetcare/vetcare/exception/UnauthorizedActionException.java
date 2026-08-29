package com.vetcare.vetcare.exception;

public class UnauthorizedActionException extends BusinessException {

    public UnauthorizedActionException(String mensaje) {
        super(mensaje);
    }
}