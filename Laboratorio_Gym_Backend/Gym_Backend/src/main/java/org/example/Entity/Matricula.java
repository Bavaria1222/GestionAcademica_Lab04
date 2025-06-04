package org.example.Entity;

public class Matricula {
    private int idMatricula;
    private String cedulaAlumno;
    private int idGrupo;
    private Float nota;

    // Constructor por defecto
    public Matricula() {
    }

    // Constructor sin idMatricula (para inserción)
    public Matricula(String cedulaAlumno, int idGrupo, Float nota) {
        this.cedulaAlumno = cedulaAlumno;
        this.idGrupo = idGrupo;
        this.nota = nota;
    }

    // Constructor con idMatricula (para consultas)
    public Matricula(int idMatricula, String cedulaAlumno, int idGrupo, Float nota) {
        this.idMatricula = idMatricula;
        this.cedulaAlumno = cedulaAlumno;
        this.idGrupo = idGrupo;
        this.nota = nota;
    }

    // Getters y Setters
    public int getIdMatricula() {
        return idMatricula;
    }

    public void setIdMatricula(int idMatricula) {
        this.idMatricula = idMatricula;
    }

    public String getCedulaAlumno() {
        return cedulaAlumno;
    }

    public void setCedulaAlumno(String cedulaAlumno) {
        this.cedulaAlumno = cedulaAlumno;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Float getNota() {
        return nota;
    }

    public void setNota(Float nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return "Matricula{" +
                "idMatricula=" + idMatricula +
                ", cedulaAlumno='" + cedulaAlumno + '\'' +
                ", idGrupo=" + idGrupo +
                ", nota=" + nota +
                '}';
    }
}
