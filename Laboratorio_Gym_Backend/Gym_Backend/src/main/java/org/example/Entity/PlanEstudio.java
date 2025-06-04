package org.example.Entity;

public class PlanEstudio {
    private int idPlanEstudio;
    private int idCarrera;
    private int idCurso;
    private int anio;
    private int ciclo;

    // Constructor por defecto
    public PlanEstudio() {
    }

    // Constructor sobrecargado
    public PlanEstudio(int idPlanEstudio, int idCarrera, int idCurso, int anio, int ciclo) {
        this.idPlanEstudio = idPlanEstudio;
        this.idCarrera = idCarrera;
        this.idCurso = idCurso;
        this.anio = anio;
        this.ciclo = ciclo;
    }

    // Getters y Setters
    public int getIdPlanEstudio() {
        return idPlanEstudio;
    }

    public void setIdPlanEstudio(int idPlanEstudio) {
        this.idPlanEstudio = idPlanEstudio;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCiclo() {
        return ciclo;
    }

    public void setCiclo(int ciclo) {
        this.ciclo = ciclo;
    }

    @Override
    public String toString() {
        return "PlanEstudio{" +
                "idPlanEstudio=" + idPlanEstudio +
                ", idCarrera=" + idCarrera +
                ", idCurso=" + idCurso +
                ", anio=" + anio +
                ", ciclo=" + ciclo +
                '}';
    }
}
