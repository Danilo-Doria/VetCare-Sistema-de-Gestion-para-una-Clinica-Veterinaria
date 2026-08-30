package com.vetcare.vetcare.service;

import com.vetcare.vetcare.model.Medicamento;
import java.util.List;

public interface MedicamentoService {

    Medicamento registrar(Medicamento medicamento);

    Medicamento buscarPorId(Integer id);

    List<Medicamento> listar();

    Medicamento actualizar(Medicamento medicamento);

    void desactivar(Integer id);

    List<Medicamento> consultarBajoInventario();
}
