package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoEnum;

public abstract class Persona {

    private int id;
    private String tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombreCompleto;
    private EstadoEnum estado;

    protected Persona(int id, String tipoIdentificacion, String numeroIdentificacion, String nombreCompleto, EstadoEnum estado) {
        this.id = id;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", tipoIdentificacion="
                + tipoIdentificacion + ", numeroIdentificacion="
                + numeroIdentificacion + ", nombreCompleto="
                + nombreCompleto + ", estado=" + estado + '}';
    }
}
