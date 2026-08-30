package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.UsuarioController;
import com.vetcare.vetcare.controller.VeterinarioController;
import com.vetcare.vetcare.model.Usuario;
import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.enums.RolEnum;

import javax.swing.JOptionPane;
import java.util.List;

public class MenuUsuario {

    private final UsuarioController usuarioController;
    private final VeterinarioController veterinarioController;

    public MenuUsuario(UsuarioController usuarioController, VeterinarioController veterinarioController) {
        this.usuarioController = usuarioController;
        this.veterinarioController = veterinarioController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Registrar", "Consultar", "Actualizar", "Activar o desactivar", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Usuarios", "VetCare",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
            );

            if (seleccion == null || seleccion.equals("Volver")) {
                volver = true;
                continue;
            }

            switch (seleccion) {
                case "Registrar" ->
                    registrar();
                case "Consultar" ->
                    consultar();
                case "Actualizar" ->
                    actualizar();
                case "Activar o desactivar" ->
                    desactivar();
            }
        }
    }

    private void registrar() {
        String tipoIdentificacion = JOptionPane.showInputDialog("Tipo de identificación:").trim();
        String numeroIdentificacion = JOptionPane.showInputDialog("Número de identificación:").trim();
        String nombreCompleto = JOptionPane.showInputDialog("Nombre completo:").trim();
        String nombreUsuario = JOptionPane.showInputDialog("Nombre de usuario:").trim();
        String contrasena = JOptionPane.showInputDialog("Contraseña:").trim();

        String rolTexto = JOptionPane.showInputDialog("Rol (ADMIN, RECEPCIONISTA, VETERINARIO):").trim().toUpperCase();
        RolEnum rol = RolEnum.valueOf(rolTexto);

        Veterinario veterinario = null;
        if (rol == RolEnum.VETERINARIO) {
            String veterinarioIdTexto = JOptionPane.showInputDialog("ID del veterinario asociado:").trim();
            veterinario = veterinarioController.buscarPorId(Integer.parseInt(veterinarioIdTexto));
        }

        Usuario usuario = new Usuario(
                nombreUsuario, contrasena, rol, veterinario,
                0, tipoIdentificacion, numeroIdentificacion, nombreCompleto,
                EstadoEnum.ACTIVO
        );

        Usuario guardado = usuarioController.registrar(usuario);
        JOptionPane.showMessageDialog(null, "Usuario registrado con éxito. ID: " + guardado.getId());
    }

    private void consultar() {
        List<Usuario> usuarios = usuarioController.listar();
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios registrados.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Usuario u : usuarios) {
            sb.append("ID: ").append(u.getId())
                    .append(" | Usuario: ").append(u.getNombreUsuario())
                    .append(" | Nombre: ").append(u.getNombreCompleto())
                    .append(" | Rol: ").append(u.getRol())
                    .append(" | Estado: ").append(u.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Usuarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizar() {
        String idTexto = JOptionPane.showInputDialog("ID del usuario a actualizar:").trim();
        Integer id = Integer.parseInt(idTexto);

        Usuario existente = usuarioController.buscarPorId(id);

        String contrasena = JOptionPane.showInputDialog("Nueva contraseña:", existente.getContrasena()).trim();
        existente.setContrasena(contrasena);

        usuarioController.actualizar(existente);
        JOptionPane.showMessageDialog(null, "Usuario actualizado con éxito.");
    }

    private void desactivar() {
        String idTexto = JOptionPane.showInputDialog("ID del usuario a desactivar:").trim();
        Integer id = Integer.parseInt(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de desactivar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            usuarioController.desactivar(id);
            JOptionPane.showMessageDialog(null, "Usuario desactivado.");
        }
    }
}
