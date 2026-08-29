package com.vetcare.vetcare.exception;

public class MedicalRecordAlreadyExistsException extends BusinessException {

    public MedicalRecordAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}
