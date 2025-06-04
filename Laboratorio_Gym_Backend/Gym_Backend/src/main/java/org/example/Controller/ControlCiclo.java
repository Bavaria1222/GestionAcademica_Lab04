package org.example.Controller;

import org.example.Entity.Ciclo;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioCiclo;

import java.util.Collection;

public class ControlCiclo {

    private ServicioCiclo servicioCiclo;

    public ControlCiclo() {
        this.servicioCiclo = new ServicioCiclo();
    }

    // Inserta un nuevo ciclo
    public void insertarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        servicioCiclo.insertarCiclo(ciclo);
    }

    // Lista todos los ciclos
    public Collection<Ciclo> listarCiclo() throws GlobalException, NoDataException {
        return servicioCiclo.listarCiclo();
    }

    // Busca un ciclo por su id
    public Ciclo buscarCiclo(int id) throws GlobalException, NoDataException {
        return servicioCiclo.buscarCiclo(id);
    }

    // Modifica un ciclo
    public void modificarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        servicioCiclo.modificarCiclo(ciclo);
    }

    // Elimina un ciclo por id
    public void eliminarCiclo(int id) throws GlobalException, NoDataException {
        servicioCiclo.eliminarCiclo(id);
    }
}
