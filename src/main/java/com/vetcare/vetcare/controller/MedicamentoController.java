package com.vetcare.vetcare.controller;

import com.vetcare.vetcare.model.Medicamento;
import com.vetcare.vetcare.service.MedicamentoService;
import java.util.List;

public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    public Medicamento registrar(Medicamento medicamento) {
        return medicamentoService.registrar(medicamento);
    }

    public Medicamento buscarPorId(Integer id) {
        return medicamentoService.buscarPorId(id);
    }

    public List<Medicamento> listar() {
        return medicamentoService.listar();
    }

    public Medicamento actualizar(Medicamento medicamento) {
        return medicamentoService.actualizar(medicamento);
    }

    public void desactivar(Integer id) {
        medicamentoService.desactivar(id);
    }

    public List<Medicamento> consultarBajoInventario() {
        return medicamentoService.consultarBajoInventario();
    }
}
