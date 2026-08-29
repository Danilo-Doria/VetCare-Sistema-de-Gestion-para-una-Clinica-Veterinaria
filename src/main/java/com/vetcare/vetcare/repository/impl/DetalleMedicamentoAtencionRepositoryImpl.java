package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.repository.AtencionMedicaRepository;
import com.vetcare.vetcare.repository.DetalleMedicamentoAtencionRepository;
import com.vetcare.vetcare.repository.MedicamentoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetalleMedicamentoAtencionRepositoryImpl implements DetalleMedicamentoAtencionRepository {

    private final AtencionMedicaRepository atencionMedicaRepository = new AtencionMedicaRepositoryImpl();
    private final MedicamentoRepository medicamentoRepository = new MedicamentoRepositoryImpl();

    @Override
    public DetalleMedicamentoAtencion guardar(DetalleMedicamentoAtencion entidad) throws PersistenciaException {
        String sql = "INSERT INTO detalles_medicamento_atencion (atencion_medica_id, medicamento_id, cantidad_utilizada) "
                + "VALUES (?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entidad.getAtencionMedica().getId());
            ps.setInt(2, entidad.getMedicamento().getId());
            ps.setInt(3, entidad.getCantidadUtilizada());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el detalle de medicamento", e);
        }
    }

    @Override
    public Optional<DetalleMedicamentoAtencion> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM detalles_medicamento_atencion WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearDetalle(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar el detalle.", e);
        }
    }

    @Override
    public List<DetalleMedicamentoAtencion> listar() throws PersistenciaException {
        String sql = "SELECT * FROM detalles_medicamento_atencion";
        List<DetalleMedicamentoAtencion> detalles = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                detalles.add(mapearDetalle(rs));
            }
            return detalles;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar detalles.", e);
        }
    }

    @Override
    public void actualizar(DetalleMedicamentoAtencion entidad) throws PersistenciaException {
        String sql = "UPDATE detalles_medicamento_atencion SET cantidad_utilizada = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entidad.getCantidadUtilizada());
            ps.setInt(2, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el detalle.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        throw new UnsupportedOperationException("Los detalles de medicamento no se desactivan.");
    }

    @Override
    public List<DetalleMedicamentoAtencion> buscarPorAtencion(Integer atencionId) throws PersistenciaException {
        String sql = "SELECT * FROM detalles_medicamento_atencion WHERE atencion_medica_id = ?";
        List<DetalleMedicamentoAtencion> detalles = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, atencionId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    detalles.add(mapearDetalle(rs));
                }
            }
            return detalles;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar detalles por atención.", e);
        }
    }

    // --- Método privado de apoyo ---
    private DetalleMedicamentoAtencion mapearDetalle(ResultSet rs) throws SQLException, PersistenciaException {
        int id = rs.getInt("id");
        int atencionId = rs.getInt("atencion_medica_id");
        int medicamentoId = rs.getInt("medicamento_id");

        AtencionMedica atencion = atencionMedicaRepository.buscarPorId(atencionId)
                .orElseThrow(() -> new PersistenciaException("Atención no encontrada para el detalle con id " + id));
        Medicamento medicamento = medicamentoRepository.buscarPorId(medicamentoId)
                .orElseThrow(() -> new PersistenciaException("Medicamento no encontrado para el detalle con id " + id));

        return new DetalleMedicamentoAtencion(id, atencion, medicamento, rs.getInt("cantidad_utilizada"));
    }
}
