package com.vetcare.vetcare.exception;

public class DuplicateOwnerDocumentException extends BusinessException {

    public DuplicateOwnerDocumentException(String mensaje) {
        super(mensaje);
    }
}
