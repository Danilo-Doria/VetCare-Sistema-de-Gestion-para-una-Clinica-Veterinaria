package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Cita;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends CrudRepository<Cita, Integer> {

    List<Cita> buscarPorIdMascota(Integer mascotaId) throws PersistenciaException;

    List<Cita> buscarPorIdVeterinario(Integer veterinarioId) throws PersistenciaException;

    List<Cita> buscarPorFecha(LocalDateTime fecha) throws PersistenciaException;

    boolean existePorVeterinarioFecha(Integer veterinarioId, LocalDateTime fecha) throws PersistenciaException;

    boolean existePorMascotaFecha(Integer mascotaId, LocalDateTime fecha) throws PersistenciaException;
}
