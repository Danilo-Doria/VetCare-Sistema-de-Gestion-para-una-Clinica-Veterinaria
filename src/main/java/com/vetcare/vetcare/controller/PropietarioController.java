package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.Propietario;
import com.vetcare.vetcare.service.PropietarioService;
import java.util.List;

public class PropietarioController {

    private final PropietarioService propietarioService;

    public PropietarioController(PropietarioService propietarioService) {
        this.propietarioService = propietarioService;
    }

    public Propietario registrar(Propietario propietario) {
        return propietarioService.registrar(propietario);
    }

    public Propietario buscarPorId(Integer id) {
        return propietarioService.buscarPorId(id);
    }

    public List<Propietario> listar() {
        return propietarioService.listar();
    }

    public Propietario actualizar(Propietario propietario) {
        return propietarioService.actualizar(propietario);
    }

    public void desactivar(Integer id) {
        propietarioService.desactivar(id);
    }

    public Propietario buscarPorNumeroIdentificacion(String numeroIdentificacion) {
        return propietarioService.buscarPorNumeroIdentificacion(numeroIdentificacion);
    }
}
