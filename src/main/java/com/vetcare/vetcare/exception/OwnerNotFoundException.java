package com.vetcare.vetcare.exception;

public class OwnerNotFoundException extends BusinessException {
    public OwnerNotFoundException(String mensaje) {
        super(mensaje);
    }
}
