package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.Cita;
import com.vetcare.vetcare.model.enums.EstadoCitaEnum;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaService {

    Cita programar(Cita cita);

    Cita buscarPorId(Integer id);

    List<Cita> listar();

    Cita actualizar(Cita cita);

    void cancelar(Integer id);

    List<Cita> buscarPorMascota(Integer mascotaId);

    List<Cita> buscarPorVeterinario(Integer veterinarioId);

    List<Cita> buscarPorFecha(LocalDateTime fecha);

    void cambiarEstado(Integer citaId, EstadoCitaEnum nuevoEstado);
}
