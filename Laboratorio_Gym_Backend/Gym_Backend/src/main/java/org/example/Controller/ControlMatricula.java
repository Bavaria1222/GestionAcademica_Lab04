package org.example.Controller;

import org.example.Entity.Matricula;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioMatricula;

import java.util.Collection;

public class ControlMatricula {

    private ServicioMatricula servicioMatricula;

    public ControlMatricula() {
        this.servicioMatricula = new ServicioMatricula();
    }

    // Inserta una nueva matrícula
    public void insertarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        servicioMatricula.insertarMatricula(matricula);
    }

    // Lista todas las matrículas
    public Collection<Matricula> listarMatricula() throws GlobalException, NoDataException {
        return servicioMatricula.listarMatricula();
    }

    public Collection<Matricula> listarMatriculaAlumno(String cedula) throws GlobalException, NoDataException {
        return servicioMatricula.listarMatriculaAlumno(cedula);
    }

    public Collection<Matricula> listarMatriculaGrupo(int idGrupo) throws GlobalException, NoDataException {
        return servicioMatricula.listarMatriculaGrupo(idGrupo);
    }

    // Busca una matrícula por id
    public Matricula buscarMatricula(int id) throws GlobalException, NoDataException {
        return servicioMatricula.buscarMatricula(id);
    }

    // Modifica una matrícula
    public void modificarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        servicioMatricula.modificarMatricula(matricula);
    }

    // Elimina una matrícula por id
    public void eliminarMatricula(int id) throws GlobalException, NoDataException {
        servicioMatricula.eliminarMatricula(id);
    }
}
