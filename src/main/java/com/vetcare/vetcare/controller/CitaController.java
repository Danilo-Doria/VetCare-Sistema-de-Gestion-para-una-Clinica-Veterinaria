package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.Cita;
import com.vetcare.vetcare.model.enums.EstadoCitaEnum;
import com.vetcare.vetcare.service.CitaService;
import java.time.LocalDateTime;
import java.util.List;

public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    public Cita programar(Cita cita) {
        return citaService.programar(cita);
    }

    public Cita buscarPorId(Integer id) {
        return citaService.buscarPorId(id);
    }

    public List<Cita> listar() {
        return citaService.listar();
    }

    public Cita actualizar(Cita cita) {
        return citaService.actualizar(cita);
    }

    public void cancelar(Integer id) {
        citaService.cancelar(id);
    }

    public List<Cita> buscarPorMascota(Integer mascotaId) {
        return citaService.buscarPorMascota(mascotaId);
    }

    public List<Cita> buscarPorVeterinario(Integer veterinarioId) {
        return citaService.buscarPorVeterinario(veterinarioId);
    }

    public List<Cita> buscarPorFecha(LocalDateTime fecha) {
        return citaService.buscarPorFecha(fecha);
    }

    public void cambiarEstado(Integer citaId, EstadoCitaEnum nuevoEstado) {
        citaService.cambiarEstado(citaId, nuevoEstado);
    }
}
