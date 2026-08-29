package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.*;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.enums.RolEnum;
import com.vetcare.vetcare.repository.UsuarioRepository;
import com.vetcare.vetcare.repository.VeterinarioRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryImpl implements UsuarioRepository {

    private final VeterinarioRepository veterinarioRepository = new VeterinarioRepositoryImpl();

    @Override
    public Usuario guardar(Usuario entidad) throws PersistenciaException {
        String sql = "INSERT INTO usuarios (tipo_identificacion, numero_identificacion, nombre_completo, "
                   + "estado, nombre_usuario, contrasena, rol, veterinario_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getTipoIdentificacion());
            ps.setString(2, entidad.getNumeroIdentificacion());
            ps.setString(3, entidad.getNombreCompleto());
            ps.setString(4, entidad.getEstado().name());
            ps.setString(5, entidad.getNombreUsuario());
            ps.setString(6, entidad.getContrasena());
            ps.setString(7, entidad.getRol().name());

            if (entidad.getVeterinario() != null) {
                ps.setInt(8, entidad.getVeterinario().getId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el usuario", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar el usuario.", e);
        }
    }

    @Override
    public List<Usuario> listar() throws PersistenciaException {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
            return usuarios;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar usuarios.", e);
        }
    }

    @Override
    public void actualizar(Usuario entidad) throws PersistenciaException {
        String sql = "UPDATE usuarios SET tipo_identificacion = ?, numero_identificacion = ?, "
                   + "nombre_completo = ?, estado = ?, nombre_usuario = ?, contrasena = ?, "
                   + "rol = ?, veterinario_id = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getTipoIdentificacion());
            ps.setString(2, entidad.getNumeroIdentificacion());
            ps.setString(3, entidad.getNombreCompleto());
            ps.setString(4, entidad.getEstado().name());
            ps.setString(5, entidad.getNombreUsuario());
            ps.setString(6, entidad.getContrasena());
            ps.setString(7, entidad.getRol().name());

            if (entidad.getVeterinario() != null) {
                ps.setInt(8, entidad.getVeterinario().getId());
            } else {
                ps.setNull(8, Types.INTEGER);
            }

            ps.setInt(9, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el usuario.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        String sql = "UPDATE usuarios SET estado = 'INACTIVO' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al desactivar el usuario.", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorNombreDeUsuario(String nombreUsuario) throws PersistenciaException {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearUsuario(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar usuario por nombre de usuario.", e);
        }
    }

    // --- Método privado de apoyo ---

    private Usuario mapearUsuario(ResultSet rs) throws SQLException, PersistenciaException {
        int veterinarioId = rs.getInt("veterinario_id");
        Veterinario veterinario = null;

        if (!rs.wasNull()) {
            veterinario = veterinarioRepository.buscarPorId(veterinarioId).orElse(null);
        }

        return new Usuario(
            rs.getString("nombre_usuario"),
            rs.getString("contrasena"),
            RolEnum.valueOf(rs.getString("rol")),
            veterinario,
            rs.getInt("id"),
            rs.getString("tipo_identificacion"),
            rs.getString("numero_identificacion"),
            rs.getString("nombre_completo"),
            EstadoEnum.valueOf(rs.getString("estado"))
        );
    }
}
