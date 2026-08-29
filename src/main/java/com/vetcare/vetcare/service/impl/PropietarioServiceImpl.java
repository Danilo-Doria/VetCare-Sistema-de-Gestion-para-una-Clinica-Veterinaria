package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.*;
import com.vetcare.vetcare.model.Propietario;
import com.vetcare.vetcare.repository.PropietarioRepository;
import com.vetcare.vetcare.service.PropietarioService;

import java.util.List;

public class PropietarioServiceImpl implements PropietarioService {

    private final PropietarioRepository propietarioRepository;

    public PropietarioServiceImpl(PropietarioRepository propietarioRepository) {
        this.propietarioRepository = propietarioRepository;
    }

    @Override
    public Propietario registrar(Propietario propietario) {
        try {
            propietarioRepository.buscarPorNumeroIdentificacion(propietario.getNumeroIdentificacion())
                    .ifPresent(p -> {
                        throw new DuplicateOwnerDocumentException(
                                "Ya existe un propietario con el número de identificación: "
                                + propietario.getNumeroIdentificacion());
                    });

            propietarioRepository.buscarPorCorreoElectronico(propietario.getCorreoElectronico())
                    .ifPresent(p -> {
                        throw new DuplicateOwnerDocumentException(
                                "Ya existe un propietario con el correo: " + propietario.getCorreoElectronico());
                    });

            return propietarioRepository.guardar(propietario);

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo completar el registro del propietario", e);
        }
    }

    @Override
    public Propietario buscarPorId(Integer id) {
        try {
            return propietarioRepository.buscarPorId(id)
                    .orElseThrow(() -> new OwnerNotFoundException(
                            "No existe un propietario con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar el propietario", e);
        }
    }

    @Override
    public List<Propietario> listar() {
        try {
            return propietarioRepository.listar();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo listar los propietarios", e);
        }
    }

    @Override
    public Propietario actualizar(Propietario propietario) {
        try {
            buscarPorId(propietario.getId());

            propietarioRepository.buscarPorCorreoElectronico(propietario.getCorreoElectronico())
                    .filter(p -> p.getId() != propietario.getId())
                    .ifPresent(p -> {
                        throw new DuplicateOwnerDocumentException(
                                "El correo " + propietario.getCorreoElectronico()
                                + " ya está en uso por otro propietario.");
                    });

            propietarioRepository.actualizar(propietario);
            return propietario;

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo actualizar el propietario", e);
        }
    }

    @Override
    public void desactivar(Integer id) {
        try {
            // Reutilizamos buscarPorId para confirmar que existe antes de desactivar
            buscarPorId(id);
            propietarioRepository.desactivar(id);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo desactivar el propietario", e);
        }
    }
}
