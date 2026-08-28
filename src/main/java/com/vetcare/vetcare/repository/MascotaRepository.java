package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Mascota;
import com.vetcare.vetcare.model.Propietario;
import java.util.List;
import java.util.Optional;

public interface MascotaRepository extends CrudRepository<Mascota, Integer>{
    List<Mascota> buscarPorNombre(String nombre) throws PersistenciaException;
    List<Mascota> buscarPorPropietario(Integer propietarioId) throws PersistenciaException;
}