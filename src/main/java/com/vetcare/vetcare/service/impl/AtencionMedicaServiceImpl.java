package com.vetcare.vetcare.service.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.*;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoAtencionEnum;
import com.vetcare.vetcare.model.enums.EstadoCitaEnum;
import com.vetcare.vetcare.repository.AtencionMedicaRepository;
import com.vetcare.vetcare.repository.CitaRepository;
import com.vetcare.vetcare.service.AtencionMedicaService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AtencionMedicaServiceImpl implements AtencionMedicaService {

    private final AtencionMedicaRepository atencionMedicaRepository;
    private final CitaRepository citaRepository;

    public AtencionMedicaServiceImpl(AtencionMedicaRepository atencionMedicaRepository,
            CitaRepository citaRepository) {
        this.atencionMedicaRepository = atencionMedicaRepository;
        this.citaRepository = citaRepository;
    }

    @Override
    public AtencionMedica iniciarAtencion(Cita cita) {
        try {
            if (cita.getEstado() != EstadoCitaEnum.CONFIRMADA) {
                throw new InvalidAppointmentStateException(
                        "Solo puede iniciarse una atención desde una cita confirmada.");
            }
            if (atencionMedicaRepository.existePorCita(cita.getId())) {
                throw new MedicalRecordAlreadyExistsException(
                        "Esta cita ya tiene una atención médica asociada.");
            }

            AtencionMedica atencion = new AtencionMedica(
                    0, cita, cita.getMascota(), cita.getVeterinario(),
                    "", null, null, null, LocalDate.now(), EstadoAtencionEnum.EN_PROCESO
            );

            cita.setEstado(EstadoCitaEnum.EN_ATENCION);
            citaRepository.actualizar(cita);

            return atencionMedicaRepository.guardar(atencion);

        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo iniciar la atención", e);
        }
    }

    /*
     * Finaliza una atención médica de forma transaccional:
     * 1. Actualiza la atención (diagnóstico, tratamiento, observaciones, estado)
     * 2. Inserta los detalles de medicamentos usados
     * 3. Descuenta el inventario de cada medicamento
     * 4. Cambia el estado de la cita a FINALIZADA
     *
     * Si CUALQUIER paso falla, TODO se revierte (rollback).
     *
     * @param medicamentosUsados mapa de medicamentoId -> cantidad utilizada
     */
    @Override
    public void finalizarAtencion(Integer atencionId, String diagnostico, String tratamiento,
            String observaciones, Map<Integer, Integer> medicamentosUsados) {

        // --- Validaciones de negocio ANTES de tocar la base de datos ---
        AtencionMedica atencion;
        try {
            atencion = atencionMedicaRepository.buscarPorId(atencionId)
                    .orElseThrow(() -> new BusinessException("No existe la atención con id: " + atencionId));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar la atención", e);
        }

        if (atencion.getEstado() == EstadoAtencionEnum.FINALIZADO) {
            throw new InvalidAppointmentStateException("Esta atención ya fue finalizada.");
        }
        if (diagnostico == null || diagnostico.isBlank()) {
            throw new BusinessException("El diagnóstico es obligatorio para finalizar la atención.");
        }
        if ((tratamiento == null || tratamiento.isBlank()) && (observaciones == null || observaciones.isBlank())) {
            throw new BusinessException("Debe registrar tratamiento u observaciones para finalizar.");
        }

        // --- Aquí empieza el manejo manual de la transacción ---
        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false); // 1. Desactivamos el autocommit

            // 2. Actualizar la atención médica
            String sqlAtencion = "UPDATE atenciones_medicas SET diagnostico = ?, tratamiento = ?, "
                    + "observaciones = ?, estado_atencion = 'FINALIZADO' WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlAtencion)) {
                ps.setString(1, diagnostico);
                ps.setString(2, tratamiento);
                ps.setString(3, observaciones);
                ps.setInt(4, atencionId);
                ps.executeUpdate();
            }

            // 3. Por cada medicamento usado: validar stock, insertar detalle, descontar inventario
            for (Map.Entry<Integer, Integer> entry : medicamentosUsados.entrySet()) {
                int medicamentoId = entry.getKey();
                int cantidadUsada = entry.getValue();

                // Verificar stock disponible (con la MISMA conexión, para que sea parte de la transacción)
                String sqlStock = "SELECT cantidad_disponible, nombre FROM medicamentos WHERE id = ?";
                int disponible;
                String nombreMedicamento;
                try (PreparedStatement ps = conn.prepareStatement(sqlStock)) {
                    ps.setInt(1, medicamentoId);
                    var rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new MedicineNotFoundException("No existe el medicamento con id: " + medicamentoId);
                    }
                    disponible = rs.getInt("cantidad_disponible");
                    nombreMedicamento = rs.getString("nombre");
                }

                if (cantidadUsada > disponible) {
                    throw new InsufficientStockException(
                            "Stock insuficiente de " + nombreMedicamento
                            + ". Disponible: " + disponible + ", solicitado: " + cantidadUsada);
                }

                // Insertar el detalle
                String sqlDetalle = "INSERT INTO detalles_medicamento_atencion "
                        + "(atencion_medica_id, medicamento_id, cantidad_utilizada) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlDetalle)) {
                    ps.setInt(1, atencionId);
                    ps.setInt(2, medicamentoId);
                    ps.setInt(3, cantidadUsada);
                    ps.executeUpdate();
                }

                // Descontar el inventario
                String sqlDescontar = "UPDATE medicamentos SET cantidad_disponible = cantidad_disponible - ? "
                        + "WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDescontar)) {
                    ps.setInt(1, cantidadUsada);
                    ps.setInt(2, medicamentoId);
                    ps.executeUpdate();
                }
            }

            // 4. Cambiar el estado de la cita a FINALIZADA
            String sqlCita = "UPDATE citas SET estado_cita = 'FINALIZADA' WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlCita)) {
                ps.setInt(1, atencion.getCita().getId());
                ps.executeUpdate();
            }

            conn.commit(); // Todo salió bien: confirmamos TODOS los cambios juntos

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Algo falló: deshacemos TODOS los cambios
                } catch (SQLException ex) {
                    throw new ErrorSistemaException("Error crítico: no se pudo revertir la transacción", ex);
                }
            }
            throw new ErrorSistemaException("No se pudo finalizar la atención médica", e);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restauramos la configuración de la conexión
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    @Override
    public List<AtencionMedica> buscarPorMascota(Integer mascotaId) {
        try {
            return atencionMedicaRepository.buscarPorMascotaId(mascotaId);
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar el historial médico", e);
        }
    }

    @Override
    public AtencionMedica buscarPorId(Integer id) {
        try {
            return atencionMedicaRepository.buscarPorId(id)
                    .orElseThrow(() -> new BusinessException("No existe la atención con id: " + id));
        } catch (PersistenciaException e) {
            throw new ErrorSistemaException("No se pudo consultar la atención", e);
        }
    }
}
