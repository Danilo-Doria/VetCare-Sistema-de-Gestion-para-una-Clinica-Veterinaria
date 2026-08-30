package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.*;
import com.vetcare.vetcare.model.*;

import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuAtencion {

    private final AtencionMedicaController atencionMedicaController;
    private final CitaController citaController;
    private final MedicamentoController medicamentoController;

    public MenuAtencion(AtencionMedicaController atencionMedicaController,
            CitaController citaController,
            MedicamentoController medicamentoController) {
        this.atencionMedicaController = atencionMedicaController;
        this.citaController = citaController;
        this.medicamentoController = medicamentoController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Iniciar atención", "Finalizar atención", "Historial por mascota", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Atenciones", "VetCare",
                    JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]
            );

            if (seleccion == null || seleccion.equals("Volver")) {
                volver = true;
                continue;
            }

            switch (seleccion) {
                case "Iniciar atención" ->
                    iniciarAtencion();
                case "Finalizar atención" ->
                    finalizarAtencion();
                case "Historial por mascota" ->
                    historialPorMascota();
            }
        }
    }

    private void iniciarAtencion() {
        String citaIdTexto = JOptionPane.showInputDialog("ID de la cita confirmada:");
        Integer citaId = Integer.parseInt(citaIdTexto);

        Cita cita = citaController.buscarPorId(citaId);
        AtencionMedica atencion = atencionMedicaController.iniciarAtencion(cita);

        JOptionPane.showMessageDialog(null, "Atención iniciada con éxito. ID de atención: " + atencion.getId());
    }

    private void finalizarAtencion() {
        String atencionIdTexto = JOptionPane.showInputDialog("ID de la atención a finalizar:");
        Integer atencionId = Integer.parseInt(atencionIdTexto);

        String sintomas = JOptionPane.showInputDialog("Síntomas observados:");
        String diagnostico = JOptionPane.showInputDialog("Diagnóstico:");
        String tratamiento = JOptionPane.showInputDialog("Tratamiento:");
        String observaciones = JOptionPane.showInputDialog("Observaciones:");

        Map<Integer, Integer> medicamentosUsados = new HashMap<>();
        boolean agregarMas = true;

        while (agregarMas) {
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Desea agregar un medicamento usado?", "Medicamentos", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                List<Medicamento> disponibles = medicamentoController.listar();
                StringBuilder lista = new StringBuilder("Medicamentos disponibles:\n");
                for (Medicamento m : disponibles) {
                    lista.append(m.getId()).append(" - ").append(m.getNombre())
                            .append(" (disponible: ").append(m.getCantidadDisponible()).append(")\n");
                }
                JOptionPane.showMessageDialog(null, lista.toString());

                String medIdTexto = JOptionPane.showInputDialog("ID del medicamento usado:");
                Integer medicamentoId = Integer.parseInt(medIdTexto);

                String cantidadTexto = JOptionPane.showInputDialog("Cantidad utilizada:");
                Integer cantidad = Integer.parseInt(cantidadTexto);

                medicamentosUsados.put(medicamentoId, cantidad);
            } else {
                agregarMas = false;
            }
        }

        atencionMedicaController.finalizarAtencion(atencionId, diagnostico, tratamiento, observaciones, medicamentosUsados);

        JOptionPane.showMessageDialog(null, "Atención finalizada con éxito. Inventario actualizado.");
    }

    private void historialPorMascota() {
        String mascotaIdTexto = JOptionPane.showInputDialog("ID de la mascota:");
        Integer mascotaId = Integer.parseInt(mascotaIdTexto);

        List<AtencionMedica> historial = atencionMedicaController.buscarPorMascota(mascotaId);

        if (historial.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Esta mascota no tiene atenciones registradas.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (AtencionMedica a : historial) {
            sb.append("Fecha: ").append(a.getFechaAtencion())
                    .append(" | Diagnóstico: ").append(a.getDiagnostico())
                    .append(" | Estado: ").append(a.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Historial Médico", JOptionPane.INFORMATION_MESSAGE);
    }
}
