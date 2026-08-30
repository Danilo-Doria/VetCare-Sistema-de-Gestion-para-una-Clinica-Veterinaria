package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.Usuario;
import com.vetcare.vetcare.service.UsuarioService;
import java.util.List;

public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario registrar(Usuario usuario) {
        return usuarioService.registrar(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioService.buscarPorId(id);
    }

    public List<Usuario> listar() {
        return usuarioService.listar();
    }

    public Usuario actualizar(Usuario usuario) {
        return usuarioService.actualizar(usuario);
    }

    public void desactivar(Integer id) {
        usuarioService.desactivar(id);
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        return usuarioService.iniciarSesion(nombreUsuario, contrasena);
    }
}
