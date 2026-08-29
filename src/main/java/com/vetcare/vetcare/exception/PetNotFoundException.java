package com.vetcare.vetcare.exception;

public class PetNotFoundException extends BusinessException {

    public PetNotFoundException(String mensaje) {
        super(mensaje);
    }
}
