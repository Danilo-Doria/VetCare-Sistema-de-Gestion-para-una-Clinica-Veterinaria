package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Propietario;
import java.util.Optional;

public interface PropietarioRepository extends CrudRepository<Propietario, Integer> {

    Optional<Propietario> buscarPorNumeroIdentificacion(String numeroIdentificacion) throws PersistenciaException;

    Optional<Propietario> buscarPorCorreoElectronico(String correoElectronico) throws PersistenciaException;
}
