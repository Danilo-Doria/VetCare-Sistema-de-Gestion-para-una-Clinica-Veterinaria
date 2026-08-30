package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.Mascota;
import com.vetcare.vetcare.service.MascotaService;
import java.util.List;

public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    public Mascota registrar(Mascota mascota) {
        return mascotaService.registrar(mascota);
    }

    public Mascota buscarPorId(Integer id) {
        return mascotaService.buscarPorId(id);
    }

    public List<Mascota> listar() {
        return mascotaService.listar();
    }

    public Mascota actualizar(Mascota mascota) {
        return mascotaService.actualizar(mascota);
    }

    public void desactivar(Integer id) {
        mascotaService.desactivar(id);
    }

    public List<Mascota> buscarPorPropietario(Integer propietarioId) {
        return mascotaService.buscarPorPropietario(propietarioId);
    }
}
