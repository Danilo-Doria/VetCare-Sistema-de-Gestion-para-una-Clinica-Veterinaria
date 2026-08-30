package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.enums.SexoEnum;
import com.vetcare.vetcare.repository.MascotaRepository;
import com.vetcare.vetcare.repository.PropietarioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MascotaRepositoryImpl implements MascotaRepository {

    private final PropietarioRepository propietarioRepository = new PropietarioRepositoryImpl();

    @Override
    public Mascota guardar(Mascota entidad) throws PersistenciaException {
        String sql = "INSERT INTO mascotas (nombre, especie, raza, sexo, fecha_nacimiento, peso, "
                + "propietario_id, estado, fecha_registro) VALUES (?, ?, ?, ?::sexo_enum, ?, ?, ?, ?::estado_enum, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecie());
            ps.setString(3, entidad.getRaza());
            ps.setString(4, entidad.getSexo().name());
            ps.setDate(5, Date.valueOf(entidad.getFechaDeNacimiento()));
            ps.setDouble(6, entidad.getPeso());
            ps.setInt(7, entidad.getPropietario().getId());
            ps.setString(8, entidad.getEstado().name());
            ps.setDate(9, Date.valueOf(entidad.getFechaRegistro()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar la mascota", e);
        }
    }

    @Override
    public Optional<Mascota> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM mascotas WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearMascota(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar la mascota.", e);
        }
    }

    @Override
    public List<Mascota> listar() throws PersistenciaException {
        String sql = "SELECT * FROM mascotas";
        List<Mascota> mascotas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mascotas.add(mapearMascota(rs));
            }
            return mascotas;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar mascotas.", e);
        }
    }

    @Override
    public void actualizar(Mascota entidad) throws PersistenciaException {
        String sql = "UPDATE mascotas SET nombre = ?, especie = ?, raza = ?, sexo = ?::sexo_enum, "
                + "fecha_nacimiento = ?, peso = ?, propietario_id = ?, estado = ?::estado_enum WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getNombre());
            ps.setString(2, entidad.getEspecie());
            ps.setString(3, entidad.getRaza());
            ps.setString(4, entidad.getSexo().name());
            ps.setDate(5, Date.valueOf(entidad.getFechaDeNacimiento()));
            ps.setDouble(6, entidad.getPeso());
            ps.setInt(7, entidad.getPropietario().getId());
            ps.setString(8, entidad.getEstado().name());
            ps.setInt(9, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar la mascota.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        String sql = "UPDATE mascotas SET estado = 'INACTIVO' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al desactivar la mascota.", e);
        }
    }

    @Override
    public List<Mascota> buscarPorNombreMascota(String nombre) throws PersistenciaException {
        String sql = "SELECT * FROM mascotas WHERE nombre = ?";
        List<Mascota> mascotas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(mapearMascota(rs));
                }
            }
            return mascotas;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar mascotas por nombre.", e);
        }
    }

    @Override
    public List<Mascota> buscarPorPropietario(Integer propietarioId) throws PersistenciaException {
        String sql = "SELECT * FROM mascotas WHERE propietario_id = ?";
        List<Mascota> mascotas = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, propietarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    mascotas.add(mapearMascota(rs));
                }
            }
            return mascotas;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar mascotas por propietario.", e);
        }
    }

    // --- Método privado de apoyo ---
    private Mascota mapearMascota(ResultSet rs) throws SQLException, PersistenciaException {
        int id = rs.getInt("id");
        int propietarioId = rs.getInt("propietario_id");

        Propietario propietario = propietarioRepository.buscarPorId(propietarioId)
                .orElseThrow(() -> new PersistenciaException(
                        "Propietario no encontrado para la mascota con id " + id));

        return new Mascota(
                id,
                rs.getString("nombre"),
                rs.getString("especie"),
                rs.getString("raza"),
                SexoEnum.valueOf(rs.getString("sexo")),
                rs.getDate("fecha_nacimiento").toLocalDate(),
                rs.getDouble("peso"),
                propietario,
                EstadoEnum.valueOf(rs.getString("estado")),
                rs.getDate("fecha_registro").toLocalDate()
        );
    }
}
