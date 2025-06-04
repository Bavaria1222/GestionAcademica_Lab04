package org.example.Controller;

import org.example.Entity.PlanEstudio;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioPlanEstudio;

import java.util.Collection;

public class ControlPlanEstudio {

    private ServicioPlanEstudio planEstudioService;

    public ControlPlanEstudio() {
        this.planEstudioService = new ServicioPlanEstudio();
    }

    // Inserta un nuevo Plan de Estudios
    public void insertarPlanEstudio(PlanEstudio plan) throws GlobalException, NoDataException {
        planEstudioService.insertarPlanEstudio(plan);
    }

    // Lista todos los registros de Plan de Estudios
    public Collection<PlanEstudio> listarPlanEstudio() throws GlobalException, NoDataException {
        return planEstudioService.listarPlanEstudio();
    }

    // Busca un Plan de Estudios por id
    public PlanEstudio buscarPlanEstudio(int id) throws GlobalException, NoDataException {
        return planEstudioService.buscarPlanEstudio(id);
    }

    // Modifica un Plan de Estudios
    public void modificarPlanEstudio(PlanEstudio plan) throws GlobalException, NoDataException {
        planEstudioService.modificarPlanEstudio(plan);
    }

    // Elimina un Plan de Estudios por id
    public void eliminarPlanEstudio(int id) throws GlobalException, NoDataException {
        planEstudioService.eliminarPlanEstudio(id);
    }
}
