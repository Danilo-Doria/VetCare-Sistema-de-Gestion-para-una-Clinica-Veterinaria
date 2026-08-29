package com.vetcare.vetcare.exception;

public class MedicineNotFoundException extends BusinessException{

    public MedicineNotFoundException(String mensaje) {
        super(mensaje);
    }
}
