package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.Usuario;
import java.util.List;

public interface UsuarioService {
    Usuario registrar(Usuario usuario);
    Usuario buscarPorId(Integer id);
    List<Usuario> listar();
    Usuario actualizar(Usuario usuario);
    void desactivar(Integer id);
    Usuario iniciarSesion(String nombreUsuario, String contrasena);
}
