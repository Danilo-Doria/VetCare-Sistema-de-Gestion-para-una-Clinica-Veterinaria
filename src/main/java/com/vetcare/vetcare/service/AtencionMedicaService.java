package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.AtencionMedica;
import com.vetcare.vetcare.model.Cita;
import java.util.List;
import java.util.Map;

public interface AtencionMedicaService {

    AtencionMedica iniciarAtencion(Cita cita);

    void finalizarAtencion(Integer atencionId, String diagnostico, String tratamiento,
            String observaciones, Map<Integer, Integer> medicamentosUsados);

    List<AtencionMedica> buscarPorMascota(Integer mascotaId);

    AtencionMedica buscarPorId(Integer id);
}
