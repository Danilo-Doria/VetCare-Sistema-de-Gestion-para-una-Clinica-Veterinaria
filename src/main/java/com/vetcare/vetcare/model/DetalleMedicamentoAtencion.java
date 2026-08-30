package com.vetcare.vetcare.model;

public class DetalleMedicamentoAtencion {

    private int id;
    private AtencionMedica atencionMedica;
    private Medicamento medicamento;
    private int cantidadUtilizada;

    public DetalleMedicamentoAtencion(int id, AtencionMedica atencionMedica, Medicamento medicamento, int cantidadDisponible) {
        this.id = id;
        this.atencionMedica = atencionMedica;
        this.medicamento = medicamento;
        this.cantidadUtilizada = cantidadUtilizada;
    }

    public int getId() {
        return id;
    }

    public AtencionMedica getAtencionMedica() {
        return atencionMedica;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public int getCantidadUtilizada() {
        return cantidadUtilizada;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAtencionMedica(AtencionMedica atencionMedica) {
        this.atencionMedica = atencionMedica;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public void setCantidadUtilizada(int cantidadUtilizada) {
        this.cantidadUtilizada = cantidadUtilizada;
    }

    @Override
    public String toString() {
        return "DetalleMedicamentoAtencion{" + "id=" + id + ", atencionMedica=" + atencionMedica + ", medicamento=" + medicamento + ", cantidadUtilizada=" + cantidadUtilizada + '}';
    }
}
