package com.vetcare.vetcare.repository;

import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {

    Optional<Usuario> buscarPorNombreDeUsuario(String nombreUsuario) throws PersistenciaException;
}
