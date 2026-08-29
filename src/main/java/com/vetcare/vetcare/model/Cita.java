package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoCitaEnum;
import java.time.LocalDateTime;

public class Cita {

    private int id;
    private Mascota mascota;
    private Veterinario veterinario;
    private LocalDateTime fechaHora;
    private String motivo;
    private EstadoCitaEnum estado;
    private LocalDateTime fechaDeCreacion;

    public Cita(int id, Mascota mascota, Veterinario veterinario, LocalDateTime fechaHora, String motivo, EstadoCitaEnum estado, LocalDateTime fechaDeCreacion) {
        this.id = id;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaDeCreacion = fechaDeCreacion;
    }

    public int getId() {
        return id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public EstadoCitaEnum getEstado() {
        return estado;
    }

    public LocalDateTime getFechaDeCreacion() {
        return fechaDeCreacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setEstado(EstadoCitaEnum estado) {
        this.estado = estado;
    }

    public void setFechaDeCreacion(LocalDateTime fechaDeCreacion) {
        this.fechaDeCreacion = fechaDeCreacion;
    }

    @Override
    public String toString() {
        return "Cita{" + "id=" + id + ", mascota=" + mascota + ", veterinario=" + veterinario + ", fechaHora=" + fechaHora + ", motivo=" + motivo + ", estado=" + estado + ", fechaDeCreacion=" + fechaDeCreacion + '}';
    }
}
