package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.exception.*;
import com.vetcare.vetcare.model.Usuario;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.repository.UsuarioRepository;
import com.vetcare.vetcare.service.UsuarioService;
import java.util.List;

public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        try {
            usuarioRepository.buscarPorNombreDeUsuario(usuario.getNombreUsuario())
                    .ifPresent(u -> {
                        throw new BusinessException(
                                "Ya existe un usuario con el nombre: " + usuario.getNombreUsuario());
                    });
            return usuarioRepository.guardar(usuario);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo registrar el usuario", e);
        }
    }

    @Override
    public Usuario buscarPorId(Integer id) {
        try {
            return usuarioRepository.buscarPorId(id)
                    .orElseThrow(() -> new BusinessException("No existe un usuario con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar el usuario", e);
        }
    }

    @Override
    public List<Usuario> listar() {
        try {
            return usuarioRepository.listar();
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo listar usuarios", e);
        }
    }

    @Override
    public Usuario actualizar(Usuario usuario) {
        try {
            buscarPorId(usuario.getId());
            usuarioRepository.actualizar(usuario);
            return usuario;
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo actualizar el usuario", e);
        }
    }

    @Override
    public void desactivar(Integer id) {
        try {
            buscarPorId(id);
            usuarioRepository.desactivar(id);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo desactivar el usuario", e);
        }
    }

    @Override
    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        try {
            Usuario usuario = usuarioRepository.buscarPorNombreDeUsuario(nombreUsuario)
                    .orElseThrow(() -> new UnauthorizedActionException("Usuario o contraseña incorrectos."));

            if (!usuario.getContrasena().equals(contrasena)) {
                throw new UnauthorizedActionException("Usuario o contraseña incorrectos.");
            }

            if (usuario.getEstado() == EstadoEnum.INACTIVO) {
                throw new UnauthorizedActionException("El usuario se encuentra inactivo.");
            }

            return usuario;

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo validar el inicio de sesión", e);
        }
    }
}
