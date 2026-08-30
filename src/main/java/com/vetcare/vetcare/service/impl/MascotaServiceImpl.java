package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.*;
import com.vetcare.vetcare.model.Mascota;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.repository.MascotaRepository;
import com.vetcare.vetcare.service.MascotaService;
import java.time.LocalDate;
import java.util.List;

public class MascotaServiceImpl implements MascotaService {

    private final MascotaRepository mascotaRepository;

    public MascotaServiceImpl(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public Mascota registrar(Mascota mascota) {
        try {

            if (mascota.getPropietario().getEstado() == EstadoEnum.INACTIVO) {
                throw new InactiveOwnerException(
                        "No se puede realizar la operación porque el propietario está inactivo."
                );
            }
            
            if (mascota.getFechaDeNacimiento().isAfter(LocalDate.now())) {
                throw new BusinessException("La fecha de nacimiento no puede ser posterior a hoy.");
            }

            // El peso debe ser mayor que cero
            if (mascota.getPeso() <= 0) {
                throw new BusinessException("El peso debe ser mayor que cero.");
            }

            return mascotaRepository.guardar(mascota);

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo registrar la mascota", e);
        }
    }

    @Override
    public Mascota buscarPorId(Integer id) {
        try {
            return mascotaRepository.buscarPorId(id)
                    .orElseThrow(() -> new PetNotFoundException("No existe una mascota con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar la mascota", e);
        }
    }

    @Override
    public List<Mascota> listar() {
        try {
            return mascotaRepository.listar();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo listar mascotas", e);
        }
    }

    @Override
    public Mascota actualizar(Mascota mascota) {
        try {
            buscarPorId(mascota.getId());
            mascotaRepository.actualizar(mascota);
            return mascota;
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo actualizar la mascota", e);
        }
    }

    @Override
    public void desactivar(Integer id) {
        try {
            buscarPorId(id);
            mascotaRepository.desactivar(id);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo desactivar la mascota", e);
        }
    }

    @Override
    public List<Mascota> buscarPorPropietario(Integer propietarioId) {
        try {
            return mascotaRepository.buscarPorPropietario(propietarioId);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo buscar mascotas por propietario", e);
        }
    }
}
