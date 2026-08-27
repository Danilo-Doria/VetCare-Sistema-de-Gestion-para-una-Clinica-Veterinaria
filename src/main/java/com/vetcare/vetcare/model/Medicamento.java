package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoEnum;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento {
    private int id;
    private String codigo;
    private String nombre;
    private String presentacion;
    private String laboratorio;
    private int cantidadDisponible;
    private int cantidadMinima;
    private BigDecimal precioUnitario;
    private EstadoEnum estado;
    private LocalDate fechaDeRegistro;

    public Medicamento(int id, String codigo, String nombre, String presentacion, String laboratorio, int cantidadDisponible, int cantidadMinima, BigDecimal precioUnitario, EstadoEnum estado, LocalDate fechaDeRegistro) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.laboratorio = laboratorio;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadMinima = cantidadMinima;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public int getCantidadMinima() {
        return cantidadMinima;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public LocalDate getFechaDeRegistro() {
        return fechaDeRegistro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public void setCantidadMinima(int cantidadMinima) {
        this.cantidadMinima = cantidadMinima;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public void setFechaDeRegistro(LocalDate fechaDeRegistro) {
        this.fechaDeRegistro = fechaDeRegistro;
    }

    @Override
    public String toString() {
        return "Medicamento{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", presentacion=" + presentacion + ", laboratorio=" + laboratorio + ", cantidadDisponible=" + cantidadDisponible + ", cantidadMinima=" + cantidadMinima + ", precioUnitario=" + precioUnitario + ", estado=" + estado + ", fechaDeRegistro=" + fechaDeRegistro + '}';
    }
    
    
}
