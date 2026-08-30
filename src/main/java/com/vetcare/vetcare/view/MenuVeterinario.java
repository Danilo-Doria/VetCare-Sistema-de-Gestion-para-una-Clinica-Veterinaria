package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.VeterinarioController;
import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import java.util.List;
import javax.swing.JOptionPane;

public class MenuVeterinario {

    private final VeterinarioController veterinarioController;

    public MenuVeterinario(VeterinarioController veterinarioController) {
        this.veterinarioController = veterinarioController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Registrar", "Consultar", "Actualizar", "Activar o desactivar", "Filtrar por especialidad", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Veterinarios", "VetCare",
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
                case "Filtrar por especialidad" ->
                    filtrarPorEspecialidad();
            }
        }
    }

    private void registrar() {
        String tipoIdentificacion = JOptionPane.showInputDialog("Tipo de identificación:").trim();
        String numeroIdentificacion = JOptionPane.showInputDialog("Número de identificación:").trim();
        String nombreCompleto = JOptionPane.showInputDialog("Nombre completo:").trim();
        String tarjetaProfesional = JOptionPane.showInputDialog("Tarjeta Profesional:").trim();

        String espcialidadTexto = JOptionPane.showInputDialog("Especialidad (MEDICINA_GENERAL, \n"
                + "    CIRUGIA, \n"
                + "    DERMATOLOGIA, \n"
                + "    NUTRICION, \n"
                + "    VACUNACION, \n"
                + "    ANIMALES_EXOTICOS):").trim().toUpperCase();

        EspecialidadEnum espcialidad = EspecialidadEnum.valueOf(espcialidadTexto);
        String telefono = JOptionPane.showInputDialog("Teléfono:").trim();
        String correo = JOptionPane.showInputDialog("Correo electrónico:").trim();

        Veterinario veterinario = new Veterinario(
                tarjetaProfesional, espcialidad, telefono, correo,
                0, tipoIdentificacion, numeroIdentificacion, nombreCompleto,
                EstadoEnum.ACTIVO
        );

        Veterinario guardado = veterinarioController.registrar(veterinario);
        JOptionPane.showMessageDialog(null,
                "Veterinario registrado con éxito. ID: " + guardado.getId());
    }

    private void consultar() {
        List<Veterinario> veterinarios = veterinarioController.listar();
        if (veterinarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay veterinarios registrados.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Veterinario v : veterinarios) {
            sb.append("ID: ").append(v.getId())
                    .append(" | Numero de documento: ").append(v.getNumeroIdentificacion())
                    .append(" | Nombre Completo: ").append(v.getNombreCompleto())
                    .append(" | Tarjeta Profesional: ").append(v.getTarjetaProfesional())
                    .append(" | Especialidad: ").append(v.getEspecialidad())
                    .append(" | Telefono: ").append(v.getTelefono())
                    .append(" | Correo: ").append(v.getCorreo())
                    .append(" | Estado: ").append(v.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Veterinarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizar() {
        String idTexto = JOptionPane.showInputDialog("ID del Veterinario a actualizar:").trim();
        Integer id = Integer.parseInt(idTexto);

        Veterinario existente = veterinarioController.buscarPorId(id);

        String tipoIdentificacion = JOptionPane.showInputDialog("Tipo de identificación:").trim();
        String numeroIdentificacion = JOptionPane.showInputDialog("Número de identificación:").trim();
        String nombreCompleto = JOptionPane.showInputDialog("Nombre completo:").trim();
        String tarjetaProfesional = JOptionPane.showInputDialog("Tarjeta Profesional:").trim();

        String espcialidadTexto = JOptionPane.showInputDialog("Especialidad (MEDICINA_GENERAL, \n"
                + "    CIRUGIA, \n"
                + "    DERMATOLOGIA, \n"
                + "    NUTRICION, \n"
                + "    VACUNACION, \n"
                + "    ANIMALES_EXOTICOS):").trim().toUpperCase();

        EspecialidadEnum espcialidad = EspecialidadEnum.valueOf(espcialidadTexto);
        String telefono = JOptionPane.showInputDialog("Teléfono:").trim();
        String correo = JOptionPane.showInputDialog("Correo electrónico:").trim();

        existente.setCorreo(correo);
        existente.setEspecialidad(espcialidad);
        existente.setNombreCompleto(nombreCompleto);
        existente.setNumeroIdentificacion(numeroIdentificacion);
        existente.setTarjetaProfesional(tarjetaProfesional);
        existente.setTelefono(telefono);
        existente.setTipoIdentificacion(tipoIdentificacion);

        veterinarioController.actualizar(existente);
        JOptionPane.showMessageDialog(null, "Veterinario actualizado con éxito.");
    }

    private void desactivar() {
        String idTexto = JOptionPane.showInputDialog("ID del Veterinario a desactivar :").trim();
        Integer id = Integer.parseInt(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de desactivar este veterinario?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            veterinarioController.desactivar(id);
            JOptionPane.showMessageDialog(null, "Veterinario desactivado.");
        }
    }

    private void filtrarPorEspecialidad() {
        String especialidadTexto = JOptionPane.showInputDialog("Digite la Especialidad (MEDICINA_GENERAL, \n"
                + "    CIRUGIA, \n"
                + "    DERMATOLOGIA, \n"
                + "    NUTRICION, \n"
                + "    VACUNACION, \n"
                + "    ANIMALES_EXOTICOS): ").trim().toUpperCase();

        List<Veterinario> veterinarios = veterinarioController.buscarPorEspecialidad(EspecialidadEnum.valueOf(especialidadTexto));

        if (veterinarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay veterinarios con esa especialidad.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Veterinario v : veterinarios) {
            sb.append("ID: ").append(v.getId())
                    .append(" | Numero de documento: ").append(v.getNumeroIdentificacion())
                    .append(" | Nombre Completo: ").append(v.getNombreCompleto())
                    .append(" | Tarjeta Profesional: ").append(v.getTarjetaProfesional())
                    .append(" | Telefono: ").append(v.getTelefono())
                    .append(" | Correo: ").append(v.getCorreo())
                    .append(" | Estado: ").append(v.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Veterinarios - " + especialidadTexto, JOptionPane.INFORMATION_MESSAGE);
    }
}
