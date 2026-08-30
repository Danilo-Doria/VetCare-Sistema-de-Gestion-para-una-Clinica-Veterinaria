package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.Veterinario;
import com.vetcare.vetcare.model.enums.EspecialidadEnum;
import java.util.List;

public interface VeterinarioService {

    Veterinario registrar(Veterinario veterinario);

    Veterinario buscarPorId(Integer id);

    List<Veterinario> listar();

    Veterinario actualizar(Veterinario veterinario);

    void desactivar(Integer id);

    List<Veterinario> buscarPorEspecialidad(EspecialidadEnum especialidad);
}
