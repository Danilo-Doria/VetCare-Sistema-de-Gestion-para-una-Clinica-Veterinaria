package com.vetcare.vetcare.exception;

public class InvalidAppointmentStateException extends BusinessException {

    public InvalidAppointmentStateException(String mensaje) {
        super(mensaje);
    }
}
