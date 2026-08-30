package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.BusinessException;
import com.vetcare.vetcare.exception.DuplicateVeterinarianLicenseException;
import com.vetcare.vetcare.exception.ErrorSistemaException;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import com.vetcare.vetcare.repository.VeterinarioRepository;
import com.vetcare.vetcare.service.VeterinarioService;
import java.util.List;

public class VeterinarioServiceImpl implements VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;

    public VeterinarioServiceImpl(VeterinarioRepository veterinarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
    }

    @Override
    public Veterinario registrar(Veterinario veterinario) {
        try {
            veterinarioRepository.buscarPorNumeroIdentificacion(veterinario.getNumeroIdentificacion())
                    .ifPresent(v -> {
                        throw new BusinessException("Ya existe un veterinario con esa identificación.");
                    });
            veterinarioRepository.buscarPorTarjetaProfesional(veterinario.getTarjetaProfesional())
                    .ifPresent(v -> {
                        throw new DuplicateVeterinarianLicenseException(
                                "Ya existe un veterinario con la tarjeta profesional: "
                                + veterinario.getTarjetaProfesional());
                    });
            veterinarioRepository.buscarPorCorreoElectronico(veterinario.getCorreo())
                    .ifPresent(v -> {
                        throw new BusinessException("Ya existe un veterinario con ese correo.");
                    });
            return veterinarioRepository.guardar(veterinario);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo registrar el veterinario", e);
        }
    }

    @Override
    public Veterinario buscarPorId(Integer id) {
        try {
            return veterinarioRepository.buscarPorId(id)
                    .orElseThrow(() -> new BusinessException("No existe un veterinario con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar el veterinario", e);
        }
    }

    @Override
    public List<Veterinario> listar() {
        try {
            return veterinarioRepository.listar();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo listar veterinarios", e);
        }
    }

    @Override
    public Veterinario actualizar(Veterinario veterinario) {
        try {
            buscarPorId(veterinario.getId());
            veterinarioRepository.buscarPorTarjetaProfesional(veterinario.getTarjetaProfesional())
                    .filter(v -> v.getId() != veterinario.getId())
                    .ifPresent(v -> {
                        throw new DuplicateVeterinarianLicenseException("Esa tarjeta profesional ya está en uso.");
                    });
            veterinarioRepository.actualizar(veterinario);
            return veterinario;
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo actualizar el veterinario", e);
        }
    }

    @Override
    public void desactivar(Integer id) {
        try {
            buscarPorId(id);
            veterinarioRepository.desactivar(id);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo desactivar el veterinario", e);
        }
    }

    @Override
    public List<Veterinario> buscarPorEspecialidad(EspecialidadEnum especialidad) {
        try {
            return veterinarioRepository.buscarPorEspecialidad(especialidad);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo buscar veterinarios por especialidad", e);
        }
    }
}
