package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.enums.SexoEnum;
import java.time.LocalDate;

public class Mascota {

    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private SexoEnum sexo;
    private LocalDate fechaDeNacimiento;
    private double peso;
    private Propietario propietario;
    private EstadoEnum estado;
    private LocalDate fechaRegistro;

    public Mascota(int id, String nombre, String especie, String raza, SexoEnum sexo, LocalDate fechaDeNacimiento, double peso, Propietario propietario, EstadoEnum estado, LocalDate fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaDeNacimiento = fechaDeNacimiento;
        this.peso = peso;
        this.propietario = propietario;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public String getRaza() {
        return raza;
    }

    public SexoEnum getSexo() {
        return sexo;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public double getPeso() {
        return peso;
    }

    public Propietario getPropietario() {
        return propietario;
    }

    public EstadoEnum getEstado() {
        return estado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }

    public void setEstado(EstadoEnum estado) {
        this.estado = estado;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Mascota{" + "id=" + id + ", nombre=" + nombre + ", especie=" + especie + ", raza=" + raza + ", sexo=" + sexo + ", fechaDeNacimiento=" + fechaDeNacimiento + ", peso=" + peso + ", propietario=" + propietario + ", estado=" + estado + ", fechaRegistro=" + fechaRegistro + '}';
    }
}
