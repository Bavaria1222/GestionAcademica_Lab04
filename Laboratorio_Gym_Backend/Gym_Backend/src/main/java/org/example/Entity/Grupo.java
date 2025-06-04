package org.example.Entity;

public class Grupo {
    private int idGrupo;
    private int idCiclo;
    private int idCurso;
    private int numGrupo;
    private String horario;
    private String idProfesor;


    public Grupo() {
    }

    // Constructor sobrecargado
    public Grupo(int idGrupo, int idCiclo, int idCurso, int numGrupo, String horario, String idProfesor) {
        this.idGrupo = idGrupo;
        this.idCiclo = idCiclo;
        this.idCurso = idCurso;
        this.numGrupo = numGrupo;
        this.horario = horario;
        this.idProfesor = idProfesor;
    }

    // Getters y Setters
    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getNumGrupo() {
        return numGrupo;
    }

    public void setNumGrupo(int numGrupo) {
        this.numGrupo = numGrupo;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(String idProfesor) {
        this.idProfesor = idProfesor;
    }

    @Override
    public String toString() {
        return "Grupo{" +
                "idGrupo=" + idGrupo +
                ", idCiclo=" + idCiclo +
                ", idCurso=" + idCurso +
                ", numGrupo=" + numGrupo +
                ", horario='" + horario + '\'' +
                ", idProfesor='" + idProfesor + '\'' +
                '}';
    }
}
