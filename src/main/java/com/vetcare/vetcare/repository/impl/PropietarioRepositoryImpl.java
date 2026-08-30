package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.Propietario;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.repository.PropietarioRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PropietarioRepositoryImpl implements PropietarioRepository {

    @Override
    public Propietario guardar(Propietario entidad) throws PersistenciaException {
        String sql = "INSERT INTO propietarios (tipo_identificacion, numero_identificacion, "
                + "nombre_completo, estado, telefono, correo_electronico, direccion, fecha_registro) "
                + "VALUES (?, ?, ?, ?::estado_enum, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getTipoIdentificacion());
            ps.setString(2, entidad.getNumeroIdentificacion());
            ps.setString(3, entidad.getNombreCompleto());
            ps.setString(4, entidad.getEstado().name());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, entidad.getCorreoElectronico());
            ps.setString(7, entidad.getDireccion());
            ps.setDate(8, Date.valueOf(entidad.getFechaRegistro()));

            ps.executeUpdate();

            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el propietario", e);
        }
    }

    @Override
    public Optional<Propietario> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM propietarios WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Propietario propietario = new Propietario(
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getDate("fecha_registro").toLocalDate(),
                            rs.getInt("id"),
                            rs.getString("tipo_identificacion"),
                            rs.getString("numero_identificacion"),
                            rs.getString("nombre_completo"),
                            EstadoEnum.valueOf(rs.getString("estado"))
                    );
                    return Optional.of(propietario);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar el propietario.", e);
        }
    }

    @Override
    public List<Propietario> listar() throws PersistenciaException {
        String sql = "SELECT * FROM propietarios";
        List<Propietario> propietarios = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Propietario propietario = new Propietario(
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("direccion"),
                        rs.getDate("fecha_registro").toLocalDate(),
                        rs.getInt("id"),
                        rs.getString("tipo_identificacion"),
                        rs.getString("numero_identificacion"),
                        rs.getString("nombre_completo"),
                        EstadoEnum.valueOf(rs.getString("estado"))
                );
                propietarios.add(propietario);
            }
            return propietarios;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar propietarios.", e);
        }
    }

    @Override
    public void actualizar(Propietario entidad) throws PersistenciaException {
        String sql = "UPDATE propietarios SET tipo_identificacion = ?, numero_identificacion = ?, "
                + "nombre_completo = ?, estado = ?::estado_enum, telefono = ?, correo_electronico = ?, "
                + "direccion = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getTipoIdentificacion());
            ps.setString(2, entidad.getNumeroIdentificacion());
            ps.setString(3, entidad.getNombreCompleto());
            ps.setString(4, entidad.getEstado().name());
            ps.setString(5, entidad.getTelefono());
            ps.setString(6, entidad.getCorreoElectronico());
            ps.setString(7, entidad.getDireccion());
            ps.setInt(8, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el propietario.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        String sql = "UPDATE propietarios SET estado = 'INACTIVO' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al desactivar el propietario.", e);
        }
    }

    @Override
    public Optional<Propietario> buscarPorNumeroIdentificacion(String numeroIdentificacion) throws PersistenciaException {
        String sql = "SELECT * FROM propietarios WHERE numero_identificacion = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, numeroIdentificacion);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Propietario propietario = new Propietario(
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getDate("fecha_registro").toLocalDate(),
                            rs.getInt("id"),
                            rs.getString("tipo_identificacion"),
                            rs.getString("numero_identificacion"),
                            rs.getString("nombre_completo"),
                            EstadoEnum.valueOf(rs.getString("estado"))
                    );
                    return Optional.of(propietario);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar propietario por identificación.", e);
        }
    }

    @Override
    public Optional<Propietario> buscarPorCorreoElectronico(String correoElectronico) throws PersistenciaException {
        String sql = "SELECT * FROM propietarios WHERE correo_electronico = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correoElectronico);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Propietario propietario = new Propietario(
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getDate("fecha_registro").toLocalDate(),
                            rs.getInt("id"),
                            rs.getString("tipo_identificacion"),
                            rs.getString("numero_identificacion"),
                            rs.getString("nombre_completo"),
                            EstadoEnum.valueOf(rs.getString("estado"))
                    );
                    return Optional.of(propietario);
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar propietario por correo.", e);
        }
    }
}
