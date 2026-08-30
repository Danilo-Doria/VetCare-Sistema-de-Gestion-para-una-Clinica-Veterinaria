package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.*;
import com.vetcare.vetcare.model.Cita;
import com.vetcare.vetcare.model.enums.EstadoCitaEnum;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.repository.CitaRepository;
import com.vetcare.vetcare.service.CitaService;
import java.time.LocalDateTime;
import java.util.List;

public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;

    public CitaServiceImpl(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    @Override
    public Cita programar(Cita cita) {
        try {
            // La mascota debe estar activa
            if (cita.getMascota().getEstado() == EstadoEnum.INACTIVO) {
                throw new BusinessException("La mascota se encuentra inactiva.");
            }

            // El propietario de la mascota debe estar activo
            if (cita.getMascota().getPropietario().getEstado() == EstadoEnum.INACTIVO) {
                throw new InactiveOwnerException("El propietario de la mascota está inactivo.");
            }

            // El veterinario debe estar activo
            if (cita.getVeterinario().getEstado() == EstadoEnum.INACTIVO) {
                throw new VeterinarianNotAvailableException("El veterinario se encuentra inactivo.");
            }

            // La fecha no puede estar en el pasado
            if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
                throw new BusinessException("La fecha de la cita no puede estar en el pasado.");
            }

            // El veterinario no puede tener dos citas en la misma fecha/hora
            if (citaRepository.existePorVeterinarioFecha(cita.getVeterinario().getId(), cita.getFechaHora())) {
                throw new VeterinarianNotAvailableException(
                        "El veterinario ya tiene una cita programada en esa fecha y hora.");
            }

            // La mascota no puede tener dos citas en la misma fecha/hora
            if (citaRepository.existePorMascotaFecha(cita.getMascota().getId(), cita.getFechaHora())) {
                throw new AppointmentConflictException(
                        "La mascota ya tiene una cita programada en esa fecha y hora.");
            }

            return citaRepository.guardar(cita);

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo programar la cita", e);
        }
    }

    @Override
    public Cita buscarPorId(Integer id) {
        try {
            return citaRepository.buscarPorId(id)
                    .orElseThrow(() -> new BusinessException("No existe una cita con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar la cita", e);
        }
    }

    @Override
    public List<Cita> listar() {
        try {
            return citaRepository.listar();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo listar citas", e);
        }
    }

    @Override
    public Cita actualizar(Cita cita) {
        try {
            Cita existente = buscarPorId(cita.getId());

            // Una cita finalizada no puede modificarse
            if (existente.getEstado() == EstadoCitaEnum.FINALIZADA) {
                throw new InvalidAppointmentStateException("No se puede modificar una cita finalizada.");
            }

            citaRepository.actualizar(cita);
            return cita;

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo actualizar la cita", e);
        }
    }

    @Override
    public void cancelar(Integer id) {
        try {
            Cita cita = buscarPorId(id);

            if (cita.getEstado() == EstadoCitaEnum.FINALIZADA) {
                throw new InvalidAppointmentStateException("No se puede cancelar una cita finalizada.");
            }

            citaRepository.desactivar(id); // recuerda: internamente pone CANCELADA
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo cancelar la cita", e);
        }
    }

    @Override
    public List<Cita> buscarPorMascota(Integer mascotaId) {
        try {
            return citaRepository.buscarPorIdMascota(mascotaId);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo buscar citas por mascota", e);
        }
    }

    @Override
    public List<Cita> buscarPorVeterinario(Integer veterinarioId) {
        try {
            return citaRepository.buscarPorIdVeterinario(veterinarioId);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo buscar citas por veterinario", e);
        }
    }

    @Override
    public List<Cita> buscarPorFecha(LocalDateTime fecha) {
        try {
            return citaRepository.buscarPorFecha(fecha);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo buscar citas por fecha", e);
        }
    }

    @Override
    public void cambiarEstado(Integer citaId, EstadoCitaEnum nuevoEstado) {
        try {
            Cita cita = buscarPorId(citaId);

            // Una cita cancelada no puede iniciar atención
            if (cita.getEstado() == EstadoCitaEnum.CANCELADA && nuevoEstado == EstadoCitaEnum.EN_ATENCION) {
                throw new InvalidAppointmentStateException("Una cita cancelada no puede iniciar atención.");
            }

            // Una cita finalizada no puede modificarse
            if (cita.getEstado() == EstadoCitaEnum.FINALIZADA) {
                throw new InvalidAppointmentStateException("No se puede modificar una cita finalizada.");
            }

            cita.setEstado(nuevoEstado);
            citaRepository.actualizar(cita);

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo cambiar el estado de la cita", e);
        }
    }
}
