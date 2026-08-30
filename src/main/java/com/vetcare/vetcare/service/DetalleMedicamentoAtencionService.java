package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.DetalleMedicamentoAtencion;
import java.util.List;

public interface DetalleMedicamentoAtencionService {

    List<DetalleMedicamentoAtencion> buscarPorAtencion(Integer atencionId);
}
