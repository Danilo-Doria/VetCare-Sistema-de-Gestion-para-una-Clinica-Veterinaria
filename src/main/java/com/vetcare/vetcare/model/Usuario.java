package com.vetcare.vetcare.model;

import com.vetcare.vetcare.model.enums.EstadoEnum;
import com.vetcare.vetcare.model.enums.RolEnum;

public class Usuario extends Persona {

    private String nombreUsuario;
    private String contrasena;
    private RolEnum rol;
    private Veterinario veterinario;

    public Usuario(
            String nombreUsuario,
            String contrasena,
            RolEnum rol,
            Veterinario veterinario,
            int id,
            String tipoIdentificacion,
            String numeroIdentificacion,
            String nombreCompleto,
            EstadoEnum estado) {
        super(
                id,
                tipoIdentificacion,
                numeroIdentificacion,
                nombreCompleto,
                estado
        );
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
        this.veterinario = veterinario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public RolEnum getRol() {
        return rol;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setRol(RolEnum rol) {
        this.rol = rol;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    @Override
    public String toString() {
        return super.toString() + " Usuario{" + "nombreUsuario="
                + nombreUsuario + ", contrasena=" + contrasena + ", rol="
                + rol + ", veterinario=" + veterinario + '}';
    }
}
