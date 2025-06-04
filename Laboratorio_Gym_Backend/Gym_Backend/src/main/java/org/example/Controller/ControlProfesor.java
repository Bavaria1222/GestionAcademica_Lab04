package org.example.Controller;

import org.example.Entity.Profesor;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioProfesor;

import java.util.Collection;

public class ControlProfesor {

    private ServicioProfesor servicioProfesor;

    public ControlProfesor() {
        this.servicioProfesor = new ServicioProfesor();
    }

    // Inserta un profesor, recibiendo la clave de forma separada
    public void insertarProfesor(Profesor profesor, String clave) throws GlobalException, NoDataException {
        servicioProfesor.insertarProfesor(profesor, clave);
    }

    // Lista todos los profesores
    public Collection<Profesor> listarProfesor() throws GlobalException, NoDataException {
        return servicioProfesor.listarProfesor();
    }

    // Busca un profesor por cédula
    public Profesor buscarProfesor(String cedula) throws GlobalException, NoDataException {
        return servicioProfesor.buscarProfesor(cedula);
    }

    // Modifica un profesor; se pasa la nueva clave si es necesario
    public void modificarProfesor(Profesor profesor, String clave) throws GlobalException, NoDataException {
        servicioProfesor.modificarProfesor(profesor, clave);
    }

    // Elimina un profesor por cédula
    public void eliminarProfesor(String cedula) throws GlobalException, NoDataException {
        servicioProfesor.eliminarProfesor(cedula);
    }
}
