package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.Propietario;
import java.util.List;

public interface PropietarioService {

    Propietario registrar(Propietario propietario);

    Propietario buscarPorId(Integer id);

    List<Propietario> listar();

    Propietario actualizar(Propietario propietario);

    void desactivar(Integer id);

    Propietario buscarPorNumeroIdentificacion(String numeroIdentificacion);
}
