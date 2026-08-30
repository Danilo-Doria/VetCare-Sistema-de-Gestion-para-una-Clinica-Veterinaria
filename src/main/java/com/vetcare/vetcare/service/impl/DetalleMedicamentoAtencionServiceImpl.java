package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.ErrorSistemaException;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.DetalleMedicamentoAtencion;
import com.vetcare.vetcare.repository.DetalleMedicamentoAtencionRepository;
import com.vetcare.vetcare.service.DetalleMedicamentoAtencionService;
import java.util.List;

public class DetalleMedicamentoAtencionServiceImpl implements DetalleMedicamentoAtencionService {

    private final DetalleMedicamentoAtencionRepository detalleRepository;

    public DetalleMedicamentoAtencionServiceImpl(DetalleMedicamentoAtencionRepository detalleRepository) {
        this.detalleRepository = detalleRepository;
    }

    @Override
    public List<DetalleMedicamentoAtencion> buscarPorAtencion(Integer atencionId) {
        try {
            return detalleRepository.buscarPorAtencion(atencionId);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar los detalles de la atención", e);
        }
    }
}
