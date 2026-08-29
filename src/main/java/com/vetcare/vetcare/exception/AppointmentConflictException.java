package com.vetcare.vetcare.exception;

public class AppointmentConflictException extends BusinessException {

    public AppointmentConflictException(String mensaje) {
        super(mensaje);
    }
}
