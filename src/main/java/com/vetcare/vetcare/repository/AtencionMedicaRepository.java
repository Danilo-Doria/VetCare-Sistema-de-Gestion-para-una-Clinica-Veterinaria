package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.AtencionMedica;
import java.util.List;

public interface AtencionMedicaRepository extends CrudRepository<AtencionMedica, Integer> {

    List<AtencionMedica> buscarPorMascotaId(Integer mascotaId) throws PersistenciaException;

    boolean existePorCita(Integer citaId) throws PersistenciaException;
}
