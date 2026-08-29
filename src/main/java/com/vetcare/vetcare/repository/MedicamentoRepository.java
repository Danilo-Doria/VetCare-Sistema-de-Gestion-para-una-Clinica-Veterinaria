package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Medicamento;
import java.util.List;
import java.util.Optional;

public interface MedicamentoRepository extends CrudRepository<Medicamento, Integer> {

    Optional<Medicamento> buscarMedicamentoPorCodigo(String codigo) throws PersistenciaException;

    List<Medicamento> consultarBajoInventario() throws PersistenciaException;
}
