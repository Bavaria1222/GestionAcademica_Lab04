package org.example.Entity;

import java.time.LocalDate;

public class Ciclo {
    private int idCiclo;
    private int anio;
    private int numero;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;


    public Ciclo() {
    }


    public Ciclo(int idCiclo, int anio, int numero, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idCiclo = idCiclo;
        this.anio = anio;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }


    public Ciclo(int anio, int numero, LocalDate fechaInicio, LocalDate fechaFin) {
        this.anio = anio;
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Getters y Setters
    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Override
    public String toString() {
        return "Ciclo{" +
                "idCiclo=" + idCiclo +
                ", anio=" + anio +
                ", numero=" + numero +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}
