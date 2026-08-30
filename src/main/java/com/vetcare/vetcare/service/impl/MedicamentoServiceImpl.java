package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.*;
import com.vetcare.vetcare.model.Medicamento;
import com.vetcare.vetcare.repository.MedicamentoRepository;
import com.vetcare.vetcare.service.MedicamentoService;
import java.util.List;

public class MedicamentoServiceImpl implements MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoServiceImpl(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Override
    public Medicamento registrar(Medicamento medicamento) {
        try {
            medicamentoRepository.buscarPorCodigo(medicamento.getCodigo())
                    .ifPresent(m -> {
                        throw new BusinessException("Ya existe un medicamento con el código: " + medicamento.getCodigo());
                    });
            return medicamentoRepository.guardar(medicamento);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo registrar el medicamento", e);
        }
    }

    @Override
    public Medicamento buscarPorId(Integer id) {
        try {
            return medicamentoRepository.buscarPorId(id)
                    .orElseThrow(() -> new MedicineNotFoundException("No existe un medicamento con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar el medicamento", e);
        }
    }

    @Override
    public List<Medicamento> listar() {
        try {
            return medicamentoRepository.listar();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo listar medicamentos", e);
        }
    }

    @Override
    public Medicamento actualizar(Medicamento medicamento) {
        try {
            buscarPorId(medicamento.getId());
            medicamentoRepository.actualizar(medicamento);
            return medicamento;
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo actualizar el medicamento", e);
        }
    }

    @Override
    public void desactivar(Integer id) {
        try {
            buscarPorId(id);
            medicamentoRepository.desactivar(id);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo desactivar el medicamento", e);
        }
    }

    @Override
    public List<Medicamento> consultarBajoInventario() {
        try {
            return medicamentoRepository.consultarBajoInventario();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar inventario bajo", e);
        }
    }
}
