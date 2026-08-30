package com.vetcare.vetcare.view;

import com.vetcare.vetcare.controller.MedicamentoController;
import com.vetcare.vetcare.model.Medicamento;
import com.vetcare.vetcare.model.enums.EstadoEnum;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MenuMedicamento {

    private final MedicamentoController medicamentoController;

    public MenuMedicamento(MedicamentoController medicamentoController) {
        this.medicamentoController = medicamentoController;
    }

    public void mostrar() {
        boolean volver = false;
        while (!volver) {
            String[] opciones = {"Registrar", "Consultar", "Actualizar", "Activar o desactivar",
                "Consultar bajo inventario", "Volver"};
            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Gestión de Medicamentos", "VetCare",
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
                case "Consultar bajo inventario" ->
                    consultarBajoInventario();
            }
        }
    }

    private void registrar() {
        String codigo = JOptionPane.showInputDialog("Código:");
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String presentacion = JOptionPane.showInputDialog("Presentación:");
        String laboratorio = JOptionPane.showInputDialog("Laboratorio:");

        String cantidadDisponibleTexto = JOptionPane.showInputDialog("Cantidad disponible:");
        int cantidadDisponible = Integer.parseInt(cantidadDisponibleTexto);

        String cantidadMinimaTexto = JOptionPane.showInputDialog("Cantidad mínima:");
        int cantidadMinima = Integer.parseInt(cantidadMinimaTexto);

        String precioTexto = JOptionPane.showInputDialog("Precio unitario:");
        BigDecimal precioUnitario = new BigDecimal(precioTexto);

        Medicamento medicamento = new Medicamento(
                0, codigo, nombre, presentacion, laboratorio,
                cantidadDisponible, cantidadMinima, precioUnitario,
                EstadoEnum.ACTIVO, LocalDate.now()
        );

        Medicamento guardado = medicamentoController.registrar(medicamento);
        JOptionPane.showMessageDialog(null, "Medicamento registrado con éxito. ID: " + guardado.getId());
    }

    private void consultar() {
        List<Medicamento> medicamentos = medicamentoController.listar();
        if (medicamentos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay medicamentos registrados.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Medicamento m : medicamentos) {
            sb.append("ID: ").append(m.getId())
                    .append(" | Código: ").append(m.getCodigo())
                    .append(" | Nombre: ").append(m.getNombre())
                    .append(" | Disponible: ").append(m.getCantidadDisponible())
                    .append(" | Mínima: ").append(m.getCantidadMinima())
                    .append(" | Precio: ").append(m.getPrecioUnitario())
                    .append(" | Estado: ").append(m.getEstado())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Listado de Medicamentos", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizar() {
        String idTexto = JOptionPane.showInputDialog("ID del medicamento a actualizar:");
        Integer id = Integer.parseInt(idTexto);

        Medicamento existente = medicamentoController.buscarPorId(id);

        String cantidadTexto = JOptionPane.showInputDialog("Nueva cantidad disponible:", existente.getCantidadDisponible());
        existente.setCantidadDisponible(Integer.parseInt(cantidadTexto));

        String precioTexto = JOptionPane.showInputDialog("Nuevo precio unitario:", existente.getPrecioUnitario());
        existente.setPrecioUnitario(new BigDecimal(precioTexto));

        medicamentoController.actualizar(existente);
        JOptionPane.showMessageDialog(null, "Medicamento actualizado con éxito.");
    }

    private void desactivar() {
        String idTexto = JOptionPane.showInputDialog("ID del medicamento a desactivar:");
        Integer id = Integer.parseInt(idTexto);

        int confirmacion = JOptionPane.showConfirmDialog(null,
                "¿Está seguro de desactivar este medicamento?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            medicamentoController.desactivar(id);
            JOptionPane.showMessageDialog(null, "Medicamento desactivado.");
        }
    }

    private void consultarBajoInventario() {
        List<Medicamento> medicamentos = medicamentoController.consultarBajoInventario();
        if (medicamentos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay medicamentos con inventario bajo.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Medicamento m : medicamentos) {
            sb.append("ID: ").append(m.getId())
                    .append(" | ").append(m.getNombre())
                    .append(" | Disponible: ").append(m.getCantidadDisponible())
                    .append(" | Mínima: ").append(m.getCantidadMinima())
                    .append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Bajo Inventario", JOptionPane.WARNING_MESSAGE);
    }
}
