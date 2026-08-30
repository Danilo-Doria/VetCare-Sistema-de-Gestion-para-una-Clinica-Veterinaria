package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.repository.VeterinarioRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VeterinarioRepositoryImpl implements VeterinarioRepository {

    @Override
    public Veterinario guardar(Veterinario entidad) throws PersistenciaException {
        String sql = "INSERT INTO veterinarios (tipo_identificacion, numero_identificacion, "
                + "nombre_completo, estado, tarjeta_profesional, especialidad, telefono, correo_electronico) "
                + "VALUES (?, ?, ?, ?::estado_enum, ?, ?::especialidad_enum, ?, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, entidad.getTipoIdentificacion());
            stmt.setString(2, entidad.getNumeroIdentificacion());
            stmt.setString(3, entidad.getNombreCompleto());
            stmt.setString(4, entidad.getEstado().name());
            stmt.setString(5, entidad.getTarjetaProfesional());
            stmt.setString(6, entidad.getEspecialidad().name());
            stmt.setString(7, entidad.getTelefono());
            stmt.setString(8, entidad.getCorreo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el veterinario", e);
        }
    }

    @Override
    public Optional<Veterinario> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM veterinarios WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVeterinario(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar el veterinario.", e);
        }
    }

    @Override
    public List<Veterinario> listar() throws PersistenciaException {
        String sql = "SELECT * FROM veterinarios";
        List<Veterinario> veterinarios = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                veterinarios.add(mapearVeterinario(rs));
            }
            return veterinarios;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar veterinarios.", e);
        }
    }

    @Override
    public void actualizar(Veterinario entidad) throws PersistenciaException {
        String sql = "UPDATE veterinarios SET tipo_identificacion = ?, numero_identificacion = ?, "
                + "nombre_completo = ?, estado = ?::estado_enum, tarjeta_profesional = ?, especialidad = ?::especialidad_enum, "
                + "telefono = ?, correo_electronico = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getTipoIdentificacion());
            ps.setString(2, entidad.getNumeroIdentificacion());
            ps.setString(3, entidad.getNombreCompleto());
            ps.setString(4, entidad.getEstado().name());
            ps.setString(5, entidad.getTarjetaProfesional());
            ps.setString(6, entidad.getEspecialidad().name());
            ps.setString(7, entidad.getTelefono());
            ps.setString(8, entidad.getCorreo());
            ps.setInt(9, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el veterinario.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        String sql = "UPDATE veterinarios SET estado = 'INACTIVO' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al desactivar el veterinario.", e);
        }
    }

    @Override
    public Optional<Veterinario> buscarPorNumeroIdentificacion(String numeroIdentificacion) throws PersistenciaException {
        String sql = "SELECT * FROM veterinarios WHERE numero_identificacion = ?";
        return buscarUno(sql, numeroIdentificacion, "Error al buscar veterinario por identificación.");
    }

    @Override
    public Optional<Veterinario> buscarPorTarjetaProfesional(String tarjetaProfesional) throws PersistenciaException {
        String sql = "SELECT * FROM veterinarios WHERE tarjeta_profesional = ?";
        return buscarUno(sql, tarjetaProfesional, "Error al buscar veterinario por tarjeta profesional.");
    }

    @Override
    public Optional<Veterinario> buscarPorCorreoElectronico(String correoElectronico) throws PersistenciaException {
        String sql = "SELECT * FROM veterinarios WHERE correo_electronico = ?";
        return buscarUno(sql, correoElectronico, "Error al buscar veterinario por correo.");
    }

    @Override
    public List<Veterinario> buscarPorEspecialidad(EspecialidadEnum especialidad) throws PersistenciaException {
        String sql = "SELECT * FROM veterinarios WHERE especialidad = ?::especialidad_enum";
        List<Veterinario> veterinarios = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, especialidad.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    veterinarios.add(mapearVeterinario(rs));
                }
            }
            return veterinarios;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar veterinarios por especialidad.", e);
        }
    }

    // --- Métodos privados de apoyo, para no repetir código ---
    private Optional<Veterinario> buscarUno(String sql, String parametro, String mensajeError) throws PersistenciaException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, parametro);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearVeterinario(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException(mensajeError, e);
        }
    }

    private Veterinario mapearVeterinario(ResultSet rs) throws SQLException {
        return new Veterinario(
                rs.getString("tarjeta_profesional"),
                EspecialidadEnum.valueOf(rs.getString("especialidad")),
                rs.getString("telefono"),
                rs.getString("correo_electronico"),
                rs.getInt("id"),
                rs.getString("tipo_identificacion"),
                rs.getString("numero_identificacion"),
                rs.getString("nombre_completo"),
                EstadoEnum.valueOf(rs.getString("estado"))
        );
    }
}
