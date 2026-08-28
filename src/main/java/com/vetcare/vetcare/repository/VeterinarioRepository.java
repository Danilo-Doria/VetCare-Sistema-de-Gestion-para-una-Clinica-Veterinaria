package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import java.util.List;
import java.util.Optional;

public interface VeterinarioRepository extends CrudRepository<Veterinario, Integer> {

    Optional<Veterinario> buscarPorNumeroIdentificacion(String numeroIdentificacion) throws PersistenciaException;

    Optional<Veterinario> buscarPorTarjetaProfesional(String tarjetaProfesional) throws PersistenciaException;

    Optional<Veterinario> buscarPorCorreoElectronico(String correoElectronico) throws PersistenciaException;

    List<Veterinario> buscarPorEspecialidad(EspecialidadEnum especialidad) throws PersistenciaException;
}
