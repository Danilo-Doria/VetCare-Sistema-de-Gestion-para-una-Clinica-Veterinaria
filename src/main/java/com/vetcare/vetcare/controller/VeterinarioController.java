package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import com.vetcare.vetcare.service.VeterinarioService;
import java.util.List;

public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    public VeterinarioController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    public Veterinario registrar(Veterinario veterinario) {
        return veterinarioService.registrar(veterinario);
    }

    public Veterinario buscarPorId(Integer id) {
        return veterinarioService.buscarPorId(id);
    }

    public List<Veterinario> listar() {
        return veterinarioService.listar();
    }

    public Veterinario actualizar(Veterinario veterinario) {
        return veterinarioService.actualizar(veterinario);
    }

    public void desactivar(Integer id) {
        veterinarioService.desactivar(id);
    }

    public List<Veterinario> buscarPorEspecialidad(EspecialidadEnum especialidad) {
        return veterinarioService.buscarPorEspecialidad(especialidad);
    }
}
