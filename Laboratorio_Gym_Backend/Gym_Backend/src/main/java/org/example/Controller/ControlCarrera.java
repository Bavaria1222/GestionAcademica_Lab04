package org.example.Controller;

import org.example.Entity.Carrera;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioCarrera;

import java.util.Collection;

public class ControlCarrera {
    private ServicioCarrera servicioCarrera;

    public ControlCarrera() {
        this.servicioCarrera = new ServicioCarrera();
    }

    public void insertarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        servicioCarrera.insertarCarrera(carrera);
    }

    public Collection<Carrera> listarCarreras() throws GlobalException, NoDataException {
        return servicioCarrera.listarCarrera();
    }

    public Carrera buscarCarrera(int id) throws GlobalException, NoDataException {
        return servicioCarrera.buscarCarrera(id);
    }

    public void modificarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        servicioCarrera.modificarCarrera(carrera);
    }

    public void eliminarCarrera(int id) throws GlobalException, NoDataException {
        servicioCarrera.eliminarCarrera(id);
    }

}
