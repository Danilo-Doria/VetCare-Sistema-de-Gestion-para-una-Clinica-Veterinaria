package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.CitaController;
import com.vetcare.vetcare.controller.MascotaController;
import com.vetcare.vetcare.controller.VeterinarioController;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoCitaEnum;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.util.List;

public class MenuCita {

    private final CitaController citaController;
    private final MascotaController mascotaController;
    private final VeterinarioController veterinarioController;

    public MenuCita(CitaController citaController, MascotaController mascotaController,
            VeterinarioController veterinarioController) {
        this.citaController = citaController;
        this.mascotaController = mascotaController;
        this.veterinarioController = veterinarioController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Programar", "Listar", "Confirmar cita", "Cancelar", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Citas", "VetCare",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
            );

            if (seleccion == null || seleccion.equals("Volver")) {
                volver = true;
                continue;
            }

            switch (seleccion) {
                case "Programar" ->
                    programar();
                case "Listar" ->
                    listar();
                case "Confirmar cita" ->
                    confirmarCita();
                case "Cancelar" ->
                    cancelar();
            }
        }
    }

    private void programar() {
        String mascotaIdTexto = JOptionPane.showInputDialog("ID de la mascota:");
        Mascota mascota = mascotaController.buscarPorId(Integer.parseInt(mascotaIdTexto));

        String veterinarioIdTexto = JOptionPane.showInputDialog("ID del veterinario:");
        Veterinario veterinario = veterinarioController.buscarPorId(Integer.parseInt(veterinarioIdTexto));

        String fechaTexto = JOptionPane.showInputDialog("Fecha y hora (AAAA-MM-DDTHH:MM, ej: 2026-09-15T15:30):");
        LocalDateTime fechaHora = LocalDateTime.parse(fechaTexto);

        String motivo = JOptionPane.showInputDialog("Motivo de la cita:");

        Cita cita = new Cita(0, mascota, veterinario, fechaHora, motivo,
                EstadoCitaEnum.PROGRAMADA, LocalDateTime.now());

        Cita guardada = citaController.programar(cita);
        JOptionPane.showMessageDialog(null, "Cita programada con éxito. ID: " + guardada.getId());
    }

    private void listar() {
        List<Cita> citas = citaController.listar();
        if (citas.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay citas registradas.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Cita c : citas) {
            sb.append("ID: ").append(c.getId())
                    .append(" | Mascota: ").append(c.getMascota().getNombre())
                    .append(" | Vet: ").append(c.getVeterinario().getNombreCompleto())
                    .append(" | Fecha: ").append(c.getFechaHora())
                    .append(" | Estado: ").append(c.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Citas", JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmarCita() {
        String idTexto = JOptionPane.showInputDialog("ID de la cita a confirmar:");
        Integer id = Integer.parseInt(idTexto);
        citaController.cambiarEstado(id, EstadoCitaEnum.CONFIRMADA);
        JOptionPane.showMessageDialog(null, "Cita confirmada.");
    }

    private void cancelar() {
        String idTexto = JOptionPane.showInputDialog("ID de la cita a cancelar:");
        Integer id = Integer.parseInt(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Confirma cancelar esta cita?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            citaController.cancelar(id);
            JOptionPane.showMessageDialog(null, "Cita cancelada.");
        }
    }
}
