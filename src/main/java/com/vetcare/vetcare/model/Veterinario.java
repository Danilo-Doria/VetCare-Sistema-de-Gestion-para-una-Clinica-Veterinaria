package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import com.vetcare.vetcare.model.enums.EstadoEnum;

public class Veterinario extends Persona {

    private String tarjetaProfesional;
    private EspecialidadEnum especialidad;
    private String telefono;
    private String correo;

    public Veterinario(
            String tarjetaProfesional,
            EspecialidadEnum especialidad,
            String telefono,
            String correo,
            int id,
            String tipoIdentificacion,
            String numeroIdentificacion,
            String nombreCompleto,
            EstadoEnum estado) {
        super(
                id,
                tipoIdentificacion,
                numeroIdentificacion,
                nombreCompleto,
                estado
        );
        this.tarjetaProfesional = tarjetaProfesional;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getTarjetaProfesional() {
        return tarjetaProfesional;
    }

    public EspecialidadEnum getEspecialidad() {
        return especialidad;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setTarjetaProfesional(String tarjetaProfesional) {
        this.tarjetaProfesional = tarjetaProfesional;
    }

    public void setEspecialidad(EspecialidadEnum especialidad) {
        this.especialidad = especialidad;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String toString() {
        return super.toString() + " Veterinario{" + "tarjetaProfesional="
                + tarjetaProfesional + ", especialidad=" + especialidad
                + ", telefono=" + telefono + ", correo=" + correo + '}';
    }
}
