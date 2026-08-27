package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    T guardar(T entidad) throws PersistenciaException;
    Optional<T> buscarPorId(ID id) throws PersistenciaException;
    List<T> listar() throws PersistenciaException;
    void actualizar(T entidad) throws PersistenciaException;
    void desactivar(ID id) throws PersistenciaException;
}