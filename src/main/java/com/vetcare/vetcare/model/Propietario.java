package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoEnum;
import java.time.LocalDate;

public class Propietario extends Persona {

    private String telefono;
    private String correoElectronico;
    private String direccion;
    private LocalDate fechaRegistro;

    public Propietario(
            String telefono,
            String correoElectronico,
            String direccion,
            LocalDate fechaRegistro,
            int id,
            String tipoIdentificacion,
            String numeroIdentificacion,
            String nombreCompleto,
            EstadoEnum estado) {
        super(
                id, tipoIdentificacion,
                numeroIdentificacion,
                nombreCompleto,
                estado
        );
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return super.toString() + " Propietario{" + "telefono=" + telefono
                + ", correoElectronico=" + correoElectronico + ", direccion="
                + direccion + ", fechaRegistro=" + fechaRegistro + '}';
    }
}
