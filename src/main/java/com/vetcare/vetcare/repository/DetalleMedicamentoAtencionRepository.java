package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.DetalleMedicamentoAtencion;
import java.util.List;

public interface DetalleMedicamentoAtencionRepository extends CrudRepository<DetalleMedicamentoAtencion, Integer> {

    List<DetalleMedicamentoAtencion> buscarPorAtencion(Integer atencionId) throws PersistenciaException;
}
