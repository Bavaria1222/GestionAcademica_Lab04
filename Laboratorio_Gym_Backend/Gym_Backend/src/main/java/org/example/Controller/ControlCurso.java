package org.example.Controller;

import org.example.Entity.Curso;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioCurso;

import java.util.Collection;

public class ControlCurso {

    private ServicioCurso servicioCurso;

    public ControlCurso() {
        this.servicioCurso = new ServicioCurso();
    }

    // Inserta un nuevo Curso
    public void insertarCurso(Curso curso) throws GlobalException, NoDataException {
        servicioCurso.insertarCurso(curso);
    }

    // Lista todos los cursos
    public Collection<Curso> listarCurso() throws GlobalException, NoDataException {
        return servicioCurso.listarCurso();
    }

    // Busca un Curso por id
    public Curso buscarCurso(int id) throws GlobalException, NoDataException {
        return servicioCurso.buscarCurso(id);
    }

    // Modifica un Curso
    public void modificarCurso(Curso curso) throws GlobalException, NoDataException {
        servicioCurso.modificarCurso(curso);
    }

    // Elimina un Curso por id
    public void eliminarCurso(int id) throws GlobalException, NoDataException {
        servicioCurso.eliminarCurso(id);
    }
}
