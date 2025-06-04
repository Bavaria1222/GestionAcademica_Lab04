package org.example.Controller;

import org.example.Entity.Grupo;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioGrupo;

import java.util.Collection;

public class ControlGrupo {

    private ServicioGrupo servicioGrupo;

    public ControlGrupo() {
        this.servicioGrupo = new ServicioGrupo();
    }

    // Inserta un nuevo grupo
    public void insertarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        servicioGrupo.insertarGrupo(grupo);
    }

    // Lista todos los grupos
    public Collection<Grupo> listarGrupo() throws GlobalException, NoDataException {
        return servicioGrupo.listarGrupo();
    }

    public Collection<Grupo> listarGrupoProfesor(int idCiclo, String cedula) throws GlobalException, NoDataException {
        return servicioGrupo.listarGrupoProfesor(idCiclo, cedula);
    }

    // Busca un grupo por id
    public Grupo buscarGrupo(int id) throws GlobalException, NoDataException {
        return servicioGrupo.buscarGrupo(id);
    }

    // Modifica un grupo
    public void modificarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        servicioGrupo.modificarGrupo(grupo);
    }

    // Elimina un grupo por id
    public void eliminarGrupo(int id) throws GlobalException, NoDataException {
        servicioGrupo.eliminarGrupo(id);
    }
}
