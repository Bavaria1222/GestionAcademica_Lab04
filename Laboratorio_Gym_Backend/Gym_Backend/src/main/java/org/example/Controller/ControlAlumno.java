package org.example.Controller;

import org.example.Entity.Alumno;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioAlumno;

import java.util.Collection;

public class ControlAlumno {

    private ServicioAlumno servicioAlumno;

    public ControlAlumno() {
        this.servicioAlumno = new ServicioAlumno();
    }

    // Inserta un nuevo alumno, recibiendo la clave por separado
    public void insertarAlumno(Alumno alumno, String clave) throws GlobalException, NoDataException {
        servicioAlumno.insertarAlumno(alumno, clave);
    }

    // Lista todos los alumnos
    public Collection<Alumno> listarAlumno() throws GlobalException, NoDataException {
        return servicioAlumno.listarAlumno();
    }

    // Busca un alumno por cédula
    public Alumno buscarAlumno(String cedula) throws GlobalException, NoDataException {
        return servicioAlumno.buscarAlumno(cedula);
    }

    // Modifica los datos de un alumno
    public void modificarAlumno(Alumno alumno, String clave) throws GlobalException, NoDataException {
        servicioAlumno.modificarAlumno(alumno, clave);
    }

    // Elimina un alumno por cédula
    public void eliminarAlumno(String cedula) throws GlobalException, NoDataException {
        servicioAlumno.eliminarAlumno(cedula);
    }
}
