package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.service.AtencionMedicaService;

import java.util.List;
import java.util.Map;

public class AtencionMedicaController {

    private final AtencionMedicaService atencionMedicaService;

    public AtencionMedicaController(AtencionMedicaService atencionMedicaService) {
        this.atencionMedicaService = atencionMedicaService;
    }

    public AtencionMedica iniciarAtencion(Cita cita) {
        return atencionMedicaService.iniciarAtencion(cita);
    }

    public void finalizarAtencion(Integer atencionId, String diagnostico, String tratamiento,
            String observaciones, Map<Integer, Integer> medicamentosUsados) {
        atencionMedicaService.finalizarAtencion(atencionId, diagnostico, tratamiento, observaciones, medicamentosUsados);
    }

    public List<AtencionMedica> buscarPorMascota(Integer mascotaId) {
        return atencionMedicaService.buscarPorMascota(mascotaId);
    }

    public AtencionMedica buscarPorId(Integer id) {
        return atencionMedicaService.buscarPorId(id);
    }
}
