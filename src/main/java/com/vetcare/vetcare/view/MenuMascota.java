package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.MascotaController;
import com.vetcare.vetcare.controller.PropietarioController;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.enums.SexoEnum;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.List;

public class MenuMascota {

    private final MascotaController mascotaController;
    private final PropietarioController propietarioController;

    public MenuMascota(MascotaController mascotaController, PropietarioController propietarioController) {
        this.mascotaController = mascotaController;
        this.propietarioController = propietarioController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Registrar", "Listar", "Buscar por propietario", "Actualizar", "Desactivar", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Mascotas", "VetCare",
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
                case "Buscar por propietario" ->
                    buscarPorPropietario();
                case "Actualizar" ->
                    actualizar();
                case "Desactivar" ->
                    desactivar();
            }
        }
    }

    private void registrar() {
        String propietarioIdTexto = JOptionPane.showInputDialog("ID del propietario:");
        Integer propietarioId = Integer.parseInt(propietarioIdTexto);
        Propietario propietario = propietarioController.buscarPorId(propietarioId);

        String nombre = JOptionPane.showInputDialog("Nombre de la mascota:");
        String especie = JOptionPane.showInputDialog("Especie:");
        String raza = JOptionPane.showInputDialog("Raza:");

        String sexoTexto = JOptionPane.showInputDialog("Sexo (MACHO/HEMBRA):");
        SexoEnum sexo = SexoEnum.valueOf(sexoTexto.toUpperCase());

        String fechaTexto = JOptionPane.showInputDialog("Fecha de nacimiento (AAAA-MM-DD):");
        LocalDate fechaNacimiento = LocalDate.parse(fechaTexto);

        String pesoTexto = JOptionPane.showInputDialog("Peso (kg):");
        double peso = Double.parseDouble(pesoTexto);

        Mascota mascota = new Mascota(
                0, nombre, especie, raza, sexo, fechaNacimiento, peso,
                propietario, EstadoEnum.ACTIVO, LocalDate.now()
        );

        Mascota guardada = mascotaController.registrar(mascota);
        JOptionPane.showMessageDialog(null, "Mascota registrada con éxito. ID: " + guardada.getId());
    }

    private void listar() {
        List<Mascota> mascotas = mascotaController.listar();
        if (mascotas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay mascotas registradas.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Mascota m : mascotas) {
            sb.append("ID: ").append(m.getId())
                    .append(" | ").append(m.getNombre())
                    .append(" | ").append(m.getEspecie())
                    .append(" | Propietario: ").append(m.getPropietario().getNombreCompleto())
                    .append(" | Estado: ").append(m.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Mascotas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void buscarPorPropietario() {
        String propietarioIdTexto = JOptionPane.showInputDialog("ID del propietario:");
        Integer propietarioId = Integer.parseInt(propietarioIdTexto);

        List<Mascota> mascotas = mascotaController.buscarPorPropietario(propietarioId);
        if (mascotas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Este propietario no tiene mascotas registradas.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Mascota m : mascotas) {
            sb.append("ID: ").append(m.getId()).append(" | ").append(m.getNombre()).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void actualizar() {
        String idTexto = JOptionPane.showInputDialog("ID de la mascota a actualizar:");
        Integer id = Integer.parseInt(idTexto);
        Mascota existente = mascotaController.buscarPorId(id);

        String pesoTexto = JOptionPane.showInputDialog("Nuevo peso:", existente.getPeso());
        existente.setPeso(Double.parseDouble(pesoTexto));

        mascotaController.actualizar(existente);
        JOptionPane.showMessageDialog(null, "Mascota actualizada con éxito.");
    }

    private void desactivar() {
        String idTexto = JOptionPane.showInputDialog("ID de la mascota a desactivar:");
        Integer id = Integer.parseInt(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Confirma desactivar esta mascota?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            mascotaController.desactivar(id);
            JOptionPane.showMessageDialog(null, "Mascota desactivada.");
        }
    }
}
