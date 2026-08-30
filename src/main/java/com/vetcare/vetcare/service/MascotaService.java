package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.Mascota;
import java.util.List;

public interface MascotaService {

    Mascota registrar(Mascota mascota);

    Mascota buscarPorId(Integer id);

    List<Mascota> listar();

    Mascota actualizar(Mascota mascota);

    void desactivar(Integer id);

    List<Mascota> buscarPorPropietario(Integer propietarioId);
}
