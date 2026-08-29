package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoCitaEnum;
import com.vetcare.vetcare.repository.CitaRepository;
import com.vetcare.vetcare.repository.MascotaRepository;
import com.vetcare.vetcare.repository.VeterinarioRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CitaRepositoryImpl implements CitaRepository {

    private final MascotaRepository mascotaRepository = new MascotaRepositoryImpl();
    private final VeterinarioRepository veterinarioRepository = new VeterinarioRepositoryImpl();

    @Override
    public Cita guardar(Cita entidad) throws PersistenciaException {
        String sql = "INSERT INTO citas (mascota_id, veterinario_id, fecha_hora, motivo, estado_cita, fecha_creacion) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entidad.getMascota().getId());
            ps.setInt(2, entidad.getVeterinario().getId());
            ps.setTimestamp(3, Timestamp.valueOf(entidad.getFechaHora()));
            ps.setString(4, entidad.getMotivo());
            ps.setString(5, entidad.getEstado().name());
            ps.setTimestamp(6, Timestamp.valueOf(entidad.getFechaDeCreacion()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar la cita", e);
        }
    }

    @Override
    public Optional<Cita> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM citas WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCita(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar la cita.", e);
        }
    }

    @Override
    public List<Cita> listar() throws PersistenciaException {
        String sql = "SELECT * FROM citas";
        List<Cita> citas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                citas.add(mapearCita(rs));
            }
            return citas;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar citas.", e);
        }
    }

    @Override
    public void actualizar(Cita entidad) throws PersistenciaException {
        String sql = "UPDATE citas SET mascota_id = ?, veterinario_id = ?, fecha_hora = ?, "
                   + "motivo = ?, estado_cita = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entidad.getMascota().getId());
            ps.setInt(2, entidad.getVeterinario().getId());
            ps.setTimestamp(3, Timestamp.valueOf(entidad.getFechaHora()));
            ps.setString(4, entidad.getMotivo());
            ps.setString(5, entidad.getEstado().name());
            ps.setInt(6, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar la cita.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        // Nota: "desactivar" una cita no aplica igual que en Propietario/Veterinario.
        // Aquí lo interpretamos como cancelarla. Revisa la nota más abajo.
        String sql = "UPDATE citas SET estado_cita = 'CANCELADA' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al cancelar la cita.", e);
        }
    }

    @Override
    public List<Cita> buscarPorIdMascota(Integer mascotaId) throws PersistenciaException {
        String sql = "SELECT * FROM citas WHERE mascota_id = ?";
        return buscarLista(sql, mascotaId);
    }

    @Override
    public List<Cita> buscarPorIdVeterinario(Integer veterinarioId) throws PersistenciaException {
        String sql = "SELECT * FROM citas WHERE veterinario_id = ?";
        return buscarLista(sql, veterinarioId);
    }

    @Override
    public List<Cita> buscarPorFecha(LocalDateTime fecha) throws PersistenciaException {
        String sql = "SELECT * FROM citas WHERE fecha_hora = ?";
        List<Cita> citas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCita(rs));
                }
            }
            return citas;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar citas por fecha.", e);
        }
    }

    @Override
    public boolean existePorVeterinarioFecha(Integer veterinarioId, LocalDateTime fecha) throws PersistenciaException {
        String sql = "SELECT 1 FROM citas WHERE veterinario_id = ? AND fecha_hora = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, veterinarioId);
            ps.setTimestamp(2, Timestamp.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al validar disponibilidad del veterinario.", e);
        }
    }

    @Override
    public boolean existePorMascotaFecha(Integer mascotaId, LocalDateTime fecha) throws PersistenciaException {
        String sql = "SELECT 1 FROM citas WHERE mascota_id = ? AND fecha_hora = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mascotaId);
            ps.setTimestamp(2, Timestamp.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al validar disponibilidad de la mascota.", e);
        }
    }

    // --- Métodos privados de apoyo ---

    private List<Cita> buscarLista(String sql, Integer parametro) throws PersistenciaException {
        List<Cita> citas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, parametro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    citas.add(mapearCita(rs));
                }
            }
            return citas;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar citas.", e);
        }
    }

    private Cita mapearCita(ResultSet rs) throws SQLException, PersistenciaException {
        int id = rs.getInt("id");
        int mascotaId = rs.getInt("mascota_id");
        int veterinarioId = rs.getInt("veterinario_id");

        Mascota mascota = mascotaRepository.buscarPorId(mascotaId)
                .orElseThrow(() -> new PersistenciaException("Mascota no encontrada para la cita con id " + id));
        Veterinario veterinario = veterinarioRepository.buscarPorId(veterinarioId)
                .orElseThrow(() -> new PersistenciaException("Veterinario no encontrado para la cita con id " + id));

        return new Cita(
            id,
            mascota,
            veterinario,
            rs.getTimestamp("fecha_hora").toLocalDateTime(),
            rs.getString("motivo"),
            EstadoCitaEnum.valueOf(rs.getString("estado_cita")),
            rs.getTimestamp("fecha_creacion").toLocalDateTime()
        );
    }
}