package com.vetcare.vetcare.repository.impl;

import com.vetcare.vetcare.config.ConexionBD;
import com.vetcare.vetcare.exception.PersistenciaException;
import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.Medicamento;
import com.vetcare.vetcare.repository.MedicamentoRepository;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicamentoRepositoryImpl implements MedicamentoRepository {

    @Override
    public Medicamento guardar(Medicamento entidad) throws PersistenciaException {
        String sql = "INSERT INTO medicamentos (codigo, nombre, presentacion, laboratorio, "
                   + "cantidad_disponible, cantidad_minima, precio_unitario, estado, fecha_registro) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getCodigo());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getPresentacion());
            ps.setString(4, entidad.getLaboratorio());
            ps.setInt(5, entidad.getCantidadDisponible());
            ps.setInt(6, entidad.getCantidadMinima());
            ps.setBigDecimal(7, entidad.getPrecioUnitario());
            ps.setString(8, entidad.getEstado().name());
            ps.setDate(9, Date.valueOf(entidad.getFechaDeRegistro()));

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    entidad.setId(rs.getInt(1));
                }
            }
            return entidad;

        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el medicamento", e);
        }
    }

    @Override
    public Optional<Medicamento> buscarPorId(Integer id) throws PersistenciaException {
        String sql = "SELECT * FROM medicamentos WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearMedicamento(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar el medicamento.", e);
        }
    }

    @Override
    public List<Medicamento> listar() throws PersistenciaException {
        String sql = "SELECT * FROM medicamentos";
        List<Medicamento> medicamentos = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                medicamentos.add(mapearMedicamento(rs));
            }
            return medicamentos;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al listar medicamentos.", e);
        }
    }

    @Override
    public void actualizar(Medicamento entidad) throws PersistenciaException {
        String sql = "UPDATE medicamentos SET codigo = ?, nombre = ?, presentacion = ?, "
                   + "laboratorio = ?, cantidad_disponible = ?, cantidad_minima = ?, "
                   + "precio_unitario = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, entidad.getCodigo());
            ps.setString(2, entidad.getNombre());
            ps.setString(3, entidad.getPresentacion());
            ps.setString(4, entidad.getLaboratorio());
            ps.setInt(5, entidad.getCantidadDisponible());
            ps.setInt(6, entidad.getCantidadMinima());
            ps.setBigDecimal(7, entidad.getPrecioUnitario());
            ps.setString(8, entidad.getEstado().name());
            ps.setInt(9, entidad.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el medicamento.", e);
        }
    }

    @Override
    public void desactivar(Integer id) throws PersistenciaException {
        String sql = "UPDATE medicamentos SET estado = 'INACTIVO' WHERE id = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al desactivar el medicamento.", e);
        }
    }

    @Override
    public Optional<Medicamento> buscarPorCodigo(String codigo) throws PersistenciaException {
        String sql = "SELECT * FROM medicamentos WHERE codigo = ?";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearMedicamento(rs));
                } else {
                    return Optional.empty();
                }
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar medicamento por código.", e);
        }
    }

    @Override
    public List<Medicamento> consultarBajoInventario() throws PersistenciaException {
        String sql = "SELECT * FROM medicamentos WHERE cantidad_disponible <= cantidad_minima";
        List<Medicamento> medicamentos = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                medicamentos.add(mapearMedicamento(rs));
            }
            return medicamentos;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar medicamentos con bajo inventario.", e);
        }
    }

    // --- Método privado de apoyo ---

    private Medicamento mapearMedicamento(ResultSet rs) throws SQLException {
        return new Medicamento(
            rs.getInt("id"),
            rs.getString("codigo"),
            rs.getString("nombre"),
            rs.getString("presentacion"),
            rs.getString("laboratorio"),
            rs.getInt("cantidad_disponible"),
            rs.getInt("cantidad_minima"),
            rs.getBigDecimal("precio_unitario"),
            EstadoEnum.valueOf(rs.getString("estado")),
            rs.getDate("fecha_registro").toLocalDate()
        );
    }
}