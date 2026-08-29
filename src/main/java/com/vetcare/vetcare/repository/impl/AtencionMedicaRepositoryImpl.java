package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoAtencionEnum;
import com.vetcare.vetcare.repository.AtencionMedicaRepository;
import com.vetcare.vetcare.repository.CitaRepository;
import com.vetcare.vetcare.repository.MascotaRepository;
import com.vetcare.vetcare.repository.VeterinarioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AtencionMedicaRepositoryImpl implements AtencionMedicaRepository {

    private final CitaRepository citaRepository = new CitaRepositoryImpl();
    private final MascotaRepository mascotaRepository = new MascotaRepositoryImpl();
    private final VeterinarioRepository veterinarioRepository = new VeterinarioRepositoryImpl();

    @Override
    public AtencionMedica guardar(AtencionMedica entidad) throws PersistenciaException {
        String sql = "INSERT INTO atenciones_medicas (cita_id, mascota_id, veterinario_id, sintomas, "
                + "diagnostico, tratamiento, observaciones, fecha_atencion, estado_atencion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, entidad.getCita().getId());
            stmt.setInt(2, entidad.getMascota().getId());
            stmt.setInt(3, entidad.getVeterinario().getId());
            stmt.setString(4, entidad.getSintomas());
            stmt.setString(5, entidad.getDiagnostico());
            stmt.setString(6, entidad.getTratamiento());
            stmt.setString(7, entidad.getObservaciones());
            stmt.setDate(8, Date.valueOf(entidad.getFechaAtencion()));
            stmt.setString(9, entidad.getEstado().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar la atención médica", e);
        }
    }

    @Override
    public Optional<AtencionMedica> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM atenciones_medicas WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearAtencion(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar la atención médica.", e);
        }
    }

    @Override
    public List<AtencionMedica> listar() throws PersistenciaException {
        String sql = "SELECT * FROM atenciones_medicas";
        List<AtencionMedica> atenciones = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                atenciones.add(mapearAtencion(rs));
            }
            return atenciones;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar atenciones médicas.", e);
        }
    }

    @Override
    public void actualizar(AtencionMedica entidad) throws PersistenciaException {
        String sql = "UPDATE atenciones_medicas SET sintomas = ?, diagnostico = ?, tratamiento = ?, "
                + "observaciones = ?, estado_atencion = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getSintomas());
            ps.setString(2, entidad.getDiagnostico());
            ps.setString(3, entidad.getTratamiento());
            ps.setString(4, entidad.getObservaciones());
            ps.setString(5, entidad.getEstado().name());
            ps.setInt(6, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar la atención médica.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        // Nota: igual que con Cita, "desactivar" no tiene un equivalente natural aquí.
        // El documento no pide desactivar atenciones médicas explícitamente.
        // Dejamos el método porque lo exige la interfaz, pero podría no usarse nunca en la práctica.
        throw new UnsupportedOperationException("Las atenciones médicas no se desactivan, se finalizan.");
    }

    @Override
    public List<AtencionMedica> buscarPorMascotaId(Integer mascotaId) throws PersistenciaException {
        String sql = "SELECT * FROM atenciones_medicas WHERE mascota_id = ?";
        List<AtencionMedica> atenciones = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mascotaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    atenciones.add(mapearAtencion(rs));
                }
            }
            return atenciones;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar atenciones por mascota.", e);
        }
    }

    @Override
    public boolean existePorCita(Integer citaId) throws PersistenciaException {
        String sql = "SELECT 1 FROM atenciones_medicas WHERE cita_id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, citaId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al validar existencia de atención para la cita.", e);
        }
    }

    // --- Método privado de apoyo ---
    private AtencionMedica mapearAtencion(ResultSet rs) throws SQLException, PersistenciaException {
        int id = rs.getInt("id");
        int citaId = rs.getInt("cita_id");
        int mascotaId = rs.getInt("mascota_id");
        int veterinarioId = rs.getInt("veterinario_id");

        Cita cita = citaRepository.buscarPorId(citaId)
                .orElseThrow(() -> new PersistenciaException("Cita no encontrada para la atención con id " + id));
        Mascota mascota = mascotaRepository.buscarPorId(mascotaId)
                .orElseThrow(() -> new PersistenciaException("Mascota no encontrada para la atención con id " + id));
        Veterinario veterinario = veterinarioRepository.buscarPorId(veterinarioId)
                .orElseThrow(() -> new PersistenciaException("Veterinario no encontrado para la atención con id " + id));

        return new AtencionMedica(
                id,
                cita,
                mascota,
                veterinario,
                rs.getString("sintomas"),
                rs.getString("diagnostico"),
                rs.getString("tratamiento"),
                rs.getString("observaciones"),
                rs.getDate("fecha_atencion").toLocalDate(),
                EstadoAtencionEnum.valueOf(rs.getString("estado_atencion"))
        );
    }
}
