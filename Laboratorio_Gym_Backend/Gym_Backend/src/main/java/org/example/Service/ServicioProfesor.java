package org.example.Service;

import oracle.jdbc.internal.OracleTypes;
import org.example.Entity.Profesor;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioProfesor extends Servicio {

    // Llamadas a los procedures/funciones en Oracle
    private static final String insertarProfesor = "{call insertarProfesorCompleto(?,?,?,?,?,?)}";
    private static final String listarProfesor    = "{? = call listarProfesor()}";
    private static final String buscarProfesor    = "{? = call buscarProfesor(?)}";
    private static final String modificarProfesor = "{call modificarProfesor(?,?,?,?,?,?)}";
    private static final String eliminarProfesor  = "{call eliminarProfesor(?, ?)}";

    public ServicioProfesor() {
    }

    public void insertarProfesor(Profesor profesor, String clave) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("BD no disponible: " + e.getMessage());
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarProfesor);
            pstmt.setString(1, profesor.getCedula());
            pstmt.setString(2, clave);
            pstmt.setString(3, "PROFESOR");             // rol por defecto
            pstmt.setString(4, profesor.getNombre());
            pstmt.setString(5, profesor.getTelefono());
            pstmt.setString(6, profesor.getEmail());
            pstmt.execute();
            System.out.println("\nInserción de Profesor Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al insertar Profesor: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public Collection<Profesor> listarProfesor() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("BD no disponible: " + e.getMessage());
        }

        ArrayList<Profesor> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarProfesor);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Profesor prof = new Profesor(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                lista.add(prof);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar Profesores: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        if (!lista.isEmpty()) {
            return lista;
        } else {
            throw new NoDataException("No hay datos");
        }
    }




    public Profesor buscarProfesor(String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("BD no disponible: " + e.getMessage());
        }

        Profesor profesor = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarProfesor);
            // Usamos OracleTypes.CURSOR
            pstmt.registerOutParameter(1, OracleTypes.CURSOR);
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                profesor = new Profesor(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de Profesor: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return profesor;
    }


    public void modificarProfesor(Profesor profesor, String clave) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("BD no disponible: " + e.getMessage());
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarProfesor);
            pstmt.setString(1, profesor.getCedula());
            pstmt.setString(2, clave);
            pstmt.setString(3, profesor.getNombre());
            pstmt.setString(4, profesor.getTelefono());
            pstmt.setString(5, profesor.getEmail());
            pstmt.registerOutParameter(6, Types.INTEGER); // filas afectadas
            pstmt.execute();
            int rows = pstmt.getInt(6);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación de Profesor Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al modificar Profesor: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public void eliminarProfesor(String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("BD no disponible: " + e.getMessage());
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(eliminarProfesor);
            pstmt.setString(1, cedula);
            pstmt.registerOutParameter(2, Types.INTEGER); // filas afectadas
            pstmt.execute();
            int filasAfectadas = pstmt.getInt(2);
            if (filasAfectadas == 0) {
                throw new NoDataException("No existe ningún profesor con esa cédula");
            }
            System.out.println("\nEliminación de Profesor Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al eliminar Profesor: " + e.getMessage());
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
