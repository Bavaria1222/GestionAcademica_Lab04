package org.example.Service;

import org.example.Entity.PlanEstudio;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioPlanEstudio extends Servicio {


    private static final String insertarPE = "{call insertarPlanEstudio(?,?,?,?)}";
    private static final String listarPE = "{? = call listarPlanEstudio()}";
    private static final String buscarPE = "{? = call buscarPlanEstudio(?)}";
    private static final String modificarPE = "{call modificarPlanEstudio(?,?,?,?,?,?)}";
    private static final String eliminarPE = "{call eliminarPlanEstudio(?)}";

    public ServicioPlanEstudio() {
    }


    public void insertarPlanEstudio(PlanEstudio plan) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarPE);
            // Como idPlanEstudio se genera automáticamente, no se envía
            pstmt.setInt(1, plan.getIdCarrera());
            pstmt.setInt(2, plan.getIdCurso());
            pstmt.setInt(3, plan.getAnio());
            pstmt.setInt(4, plan.getCiclo());
            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la inserción de PlanEstudio: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public Collection<PlanEstudio> listarPlanEstudio() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        ArrayList<PlanEstudio> coleccion = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarPE);
            pstmt.registerOutParameter(1, -10); // OracleTypes.CURSOR, -10 equivale a ello
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                PlanEstudio plan = new PlanEstudio(
                        rs.getInt("idPlanEstudio"),
                        rs.getInt("idCarrera"),
                        rs.getInt("idCurso"),
                        rs.getInt("anio"),
                        rs.getInt("ciclo")
                );
                coleccion.add(plan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la consulta de PlanEstudio: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        if (!coleccion.isEmpty()) {
            return coleccion;
        } else {
            throw new NoDataException("No hay datos");
        }
    }


    public PlanEstudio buscarPlanEstudio(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        PlanEstudio plan = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarPE);
            pstmt.registerOutParameter(1, -10); // Registramos el cursor de salida
            pstmt.setInt(2, id);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                plan = new PlanEstudio(
                        rs.getInt("idPlanEstudio"),
                        rs.getInt("idCarrera"),
                        rs.getInt("idCurso"),
                        rs.getInt("anio"),
                        rs.getInt("ciclo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de PlanEstudio: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return plan;
    }


    public void modificarPlanEstudio(PlanEstudio plan) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarPE);
            pstmt.setInt(1, plan.getIdPlanEstudio());
            pstmt.setInt(2, plan.getIdCarrera());
            pstmt.setInt(3, plan.getIdCurso());
            pstmt.setInt(4, plan.getAnio());
            pstmt.setInt(5, plan.getCiclo());
            // Registrar el parámetro de salida para el update count
            pstmt.registerOutParameter(6, java.sql.Types.INTEGER);
            pstmt.execute();
            int rows = pstmt.getInt(6);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la modificación de PlanEstudio: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public void eliminarPlanEstudio(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall("{call eliminarPlanEstudio(?, ?)}");
            pstmt.setInt(1, id);
            pstmt.registerOutParameter(2, java.sql.Types.INTEGER);
            pstmt.execute();
            int filas = pstmt.getInt(2);
            if (filas == 0) {
                throw new NoDataException("No se realizó el borrado");
            }
            System.out.println("\nEliminación de PlanEstudio Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la eliminación de PlanEstudio: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


}
