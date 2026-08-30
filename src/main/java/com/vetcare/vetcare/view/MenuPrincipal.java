package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.*;
import com.vetcare.vetcare.exception.BusinessException;
import com.vetcare.vetcare.model.enums.RolEnum;
import com.vetcare.vetcare.model.Usuario;

import javax.swing.JOptionPane;

public class MenuPrincipal {

    private final PropietarioController propietarioController;
    private final VeterinarioController veterinarioController;
    private final MascotaController mascotaController;
    private final CitaController citaController;
    private final MedicamentoController medicamentoController;
    private final UsuarioController usuarioController;
    private final AtencionMedicaController atencionMedicaController;

    public MenuPrincipal(PropietarioController propietarioController,
            VeterinarioController veterinarioController,
            MascotaController mascotaController,
            CitaController citaController,
            MedicamentoController medicamentoController,
            UsuarioController usuarioController,
            AtencionMedicaController atencionMedicaController) {
        this.propietarioController = propietarioController;
        this.veterinarioController = veterinarioController;
        this.mascotaController = mascotaController;
        this.citaController = citaController;
        this.medicamentoController = medicamentoController;
        this.usuarioController = usuarioController;
        this.atencionMedicaController = atencionMedicaController;
    }

    public void mostrar() {
        Usuario usuarioLogueado = iniciarSesion();
        if (usuarioLogueado == null) {
            return; // el usuario canceló el login
        }

        boolean salir = false;
        while (!salir) {
            String[] opciones = obtenerOpcionesPorRol(usuarioLogueado.getRol());
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Bienvenido, " + usuarioLogueado.getNombreCompleto(),
                    "VetCare - Menú Principal",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (seleccion == null || seleccion.equals("Salir")) {
                salir = true;
                continue;
            }

            try {
                procesarOpcion(seleccion, usuarioLogueado);
            } catch (BusinessException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Usuario iniciarSesion() {
        String nombreUsuario = JOptionPane.showInputDialog("Nombre de usuario:").trim();
        if (nombreUsuario == null) {
            return null;
        }

        String contrasena = JOptionPane.showInputDialog("Contraseña:").trim();
        if (contrasena == null) {
            return null;
        }

        try {
            return usuarioController.iniciarSesion(nombreUsuario, contrasena);
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error de acceso", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private String[] obtenerOpcionesPorRol(RolEnum rol) {
        switch (rol) {
            case ADMIN:
                return new String[]{"Propietarios", "Mascotas", "Veterinarios", "Citas",
                    "Medicamentos", "Atenciones", "Usuarios", "Salir"};
            case RECEPCIONISTA:
                return new String[]{"Propietarios", "Mascotas", "Citas", "Veterinarios", "Salir"};
            case VETERINARIO:
                return new String[]{"Mis citas", "Atenciones", "Salir"};
            default:
                return new String[]{"Salir"};
        }
    }

    private void procesarOpcion(String opcion, Usuario usuario) {
        switch (opcion) {
            case "Propietarios" ->
                new MenuPropietario(propietarioController).mostrar();
            case "Mascotas" ->
                new MenuMascota(mascotaController, propietarioController).mostrar();
            case "Veterinarios" ->
                new MenuVeterinario(veterinarioController).mostrar();
            case "Citas" ->
                new MenuCita(citaController, mascotaController, veterinarioController).mostrar();
            case "Medicamentos" ->
                new MenuMedicamento(medicamentoController).mostrar();
            case "Atenciones" ->
                new MenuAtencion(atencionMedicaController, citaController, medicamentoController).mostrar();
            case "Usuarios" ->
                new MenuUsuario(usuarioController, veterinarioController).mostrar();
        }
    }
}
