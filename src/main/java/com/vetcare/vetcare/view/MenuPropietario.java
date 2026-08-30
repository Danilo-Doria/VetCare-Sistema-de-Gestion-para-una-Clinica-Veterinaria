package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.PropietarioController;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.Propietario;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;

public class MenuPropietario {

    private final PropietarioController propietarioController;

    public MenuPropietario(PropietarioController propietarioController) {
        this.propietarioController = propietarioController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Registrar", "Listar", "Buscar por identificación", "Actualizar", "Desactivar", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Propietarios", "VetCare",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
            );

            if (seleccion == null || seleccion.equals("Volver")) {
                volver = true;
                continue;
            }

            switch (seleccion) {
                case "Registrar" ->
                    registrar();
                case "Listar" ->
                    listar();
                case "Buscar por identificación" ->
                    buscarPorIdentificacion();
                case "Actualizar" ->
                    actualizar();
                case "Desactivar" ->
                    desactivar();
            }
        }
    }

    private void registrar() {
        String tipoIdentificacion = JOptionPane.showInputDialog("Tipo de identificación:");
        String numeroIdentificacion = JOptionPane.showInputDialog("Número de identificación:");
        String nombreCompleto = JOptionPane.showInputDialog("Nombre completo:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        String correo = JOptionPane.showInputDialog("Correo electrónico:");
        String direccion = JOptionPane.showInputDialog("Dirección:");

        Propietario propietario = new Propietario(
                telefono, correo, direccion, LocalDate.now(),
                0, tipoIdentificacion, numeroIdentificacion, nombreCompleto, EstadoEnum.ACTIVO
        );

        Propietario guardado = propietarioController.registrar(propietario);
        JOptionPane.showMessageDialog(null,
                "Propietario registrado con éxito. ID: " + guardado.getId());
    }

    private void listar() {
        List<Propietario> propietarios = propietarioController.listar();
        if (propietarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay propietarios registrados.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Propietario p : propietarios) {
            sb.append("ID: ").append(p.getId())
                    .append(" | ").append(p.getNombreCompleto())
                    .append(" | ").append(p.getNumeroIdentificacion())
                    .append(" | Estado: ").append(p.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Propietarios", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarPorIdentificacion() {
        String numeroIdentificacion = JOptionPane.showInputDialog("Número de identificación a buscar:");
        Propietario propietario = propietarioController.buscarPorNumeroIdentificacion(numeroIdentificacion);

        JOptionPane.showMessageDialog(null,
                "ID: " + propietario.getId() + "\n"
                + "Nombre: " + propietario.getNombreCompleto() + "\n"
                + "Teléfono: " + propietario.getTelefono() + "\n"
                + "Correo: " + propietario.getCorreoElectronico() + "\n"
                + "Estado: " + propietario.getEstado()
        );
    }

    private void actualizar() {
        String idTexto = JOptionPane.showInputDialog("ID del propietario a actualizar:");
        Integer id = Integer.parseInt(idTexto);

        Propietario existente = propietarioController.buscarPorId(id);

        String telefono = JOptionPane.showInputDialog("Nuevo teléfono:", existente.getTelefono());
        String correo = JOptionPane.showInputDialog("Nuevo correo:", existente.getCorreoElectronico());
        String direccion = JOptionPane.showInputDialog("Nueva dirección:", existente.getDireccion());

        existente.setTelefono(telefono);
        existente.setCorreoElectronico(correo);
        existente.setDireccion(direccion);

        propietarioController.actualizar(existente);
        JOptionPane.showMessageDialog(null, "Propietario actualizado con éxito.");
    }

    private void desactivar() {
        String idTexto = JOptionPane.showInputDialog("ID del propietario a desactivar:");
        Integer id = Integer.parseInt(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de desactivar este propietario?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            propietarioController.desactivar(id);
            JOptionPane.showMessageDialog(null, "Propietario desactivado.");
        }
    }
}
