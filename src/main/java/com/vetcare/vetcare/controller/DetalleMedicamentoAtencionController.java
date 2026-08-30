package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.DetalleMedicamentoAtencion;
import com.vetcare.vetcare.service.DetalleMedicamentoAtencionService;
import java.util.List;

public class DetalleMedicamentoAtencionController {

    private final DetalleMedicamentoAtencionService detalleMedicamentoAtencionService;

    public DetalleMedicamentoAtencionController(DetalleMedicamentoAtencionService detalleMedicamentoAtencionService) {
        this.detalleMedicamentoAtencionService = detalleMedicamentoAtencionService;
    }

    public List<DetalleMedicamentoAtencion> buscarPorAtencion(Integer atencionId) {
        return detalleMedicamentoAtencionService.buscarPorAtencion(atencionId);
    }
}
