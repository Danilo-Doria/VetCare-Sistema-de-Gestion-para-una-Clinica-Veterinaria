package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoAtencionEnum;
import java.time.LocalDate;

public class AtencionMedica {

    private int id;
    private Cita cita;
    private Mascota mascota;
    private Veterinario veterinario;
    private String sintomas;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;
    private LocalDate fechaAtencion;
    private EstadoAtencionEnum estado;

    public AtencionMedica(int id, Cita cita, Mascota mascota, Veterinario veterinario, String sintomas, String diagnostico, String tratamiento, String observaciones, LocalDate fechaAtencion, EstadoAtencionEnum estado) {
        this.id = id;
        this.cita = cita;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
        this.fechaAtencion = fechaAtencion;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public Cita getCita() {
        return cita;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public LocalDate getFechaAtencion() {
        return fechaAtencion;
    }

    public EstadoAtencionEnum getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCita(Cita cita) {
        this.cita = cita;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setFechaAtencion(LocalDate fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public void setEstado(EstadoAtencionEnum estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "AtencionMedica{" + "id=" + id + ", cita=" + cita + ", mascota=" + mascota + ", veterinario=" + veterinario + ", sintomas=" + sintomas + ", diagnostico=" + diagnostico + ", tratamiento=" + tratamiento + ", observaciones=" + observaciones + ", fechaAtencion=" + fechaAtencion + ", estado=" + estado + '}';
    }

}
