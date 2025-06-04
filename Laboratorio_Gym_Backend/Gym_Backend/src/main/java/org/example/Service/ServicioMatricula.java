package org.example.Service;

import org.example.Entity.Matricula;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioMatricula extends Servicio {

    // Cadenas de llamada a los procedimientos y funciones en Oracle
    private static final String insertarMatricula = "{call insertarMatricula(?,?,?)}";
    private static final String listarMatricula = "{? = call listarMatricula()}";
    private static final String buscarMatricula = "{? = call buscarMatricula(?)}";
    private static final String modificarMatricula = "{call modificarMatricula(?,?,?,?,?)}";
    private static final String eliminarMatricula = "{call eliminarMatricula(?, ?)}";
    private static final String listarMatriculaAlumno = "{? = call listarMatriculaAlumno(?)}";
    private static final String listarMatriculaGrupo =
            "SELECT idMatricula, cedulaAlumno, idGrupo, nota FROM matricula WHERE idGrupo = ?";


    public ServicioMatricula() {
    }

    // Inserta una nueva matrícula en la BD
    public void insertarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("La BD no se encuentra disponible: " + e.getMessage());
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarMatricula);
            pstmt.setString(1, matricula.getCedulaAlumno());
            pstmt.setInt(2, matricula.getIdGrupo());
            if(matricula.getNota() != null) {
                pstmt.setFloat(3, matricula.getNota());
            } else {
                pstmt.setNull(3, Types.FLOAT);
            }
            pstmt.execute();
            System.out.println("\nInserción de Matrícula Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la inserción de Matrícula: " + e.getMessage());
        } finally {
            try {
                if(pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch(SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    // Lista todas las matrículas
    public Collection<Matricula> listarMatricula() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("La BD no se encuentra disponible: " + e.getMessage());
        }
        ArrayList<Matricula> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarMatricula);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while(rs.next()){
                int idMatricula = rs.getInt("idMatricula");
                String cedulaAlumno = rs.getString("cedulaAlumno");
                int idGrupo = rs.getInt("idGrupo");
                Float nota = rs.getFloat("nota");
                if(rs.wasNull()){
                    nota = null;
                }
                Matricula m = new Matricula(idMatricula, cedulaAlumno, idGrupo, nota);
                lista.add(m);
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar Matrículas: " + e.getMessage());
        } finally {
            try {
                if(rs != null)
                    rs.close();
                if(pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch(SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        if(!lista.isEmpty()){
            return lista;
        } else {
            throw new NoDataException("No hay datos");
        }
    }

    public Collection<Matricula> listarMatriculaAlumno(String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("La BD no se encuentra disponible: " + e.getMessage());
        }

        ArrayList<Matricula> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarMatriculaAlumno);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while(rs.next()){
                int idMatricula = rs.getInt("idMatricula");
                String cedulaAlumno = rs.getString("cedulaAlumno");
                int idGrupo = rs.getInt("idGrupo");
                Float nota = rs.getFloat("nota");
                if(rs.wasNull()){
                    nota = null;
                }
                lista.add(new Matricula(idMatricula, cedulaAlumno, idGrupo, nota));
            }
        } catch(SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar Matrículas: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
                if(pstmt != null) pstmt.close();
                this.desconectar();
            } catch(SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }

        if(!lista.isEmpty()) {
            return lista;
        } else {
            throw new NoDataException("No hay datos");
        }
    }

    public Collection<Matricula> listarMatriculaGrupo(int idGrupo) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }

        ArrayList<Matricula> lista = new ArrayList<>();
        ResultSet rs = null;
        java.sql.PreparedStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareStatement(listarMatriculaGrupo);
            pstmt.setInt(1, idGrupo);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idMatricula");
                String ced = rs.getString("cedulaAlumno");
                int idG = rs.getInt("idGrupo");
                Float nota = rs.getFloat("nota");
                if (rs.wasNull()) nota = null;
                lista.add(new Matricula(id, ced, idG, nota));
            }
        } catch (SQLException e) {
            throw new GlobalException("Error al listar Matrículas: " + e.getMessage());
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

    // Busca una matrícula por su id
    public Matricula buscarMatricula(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch(ClassNotFoundException e){
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch(SQLException e){
            throw new NoDataException("La BD no se encuentra disponible: " + e.getMessage());
        }
        Matricula matricula = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarMatricula);
            pstmt.registerOutParameter(1, -10);
            pstmt.setInt(2, id);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if(rs.next()){
                int idMatricula = rs.getInt("idMatricula");
                String cedulaAlumno = rs.getString("cedulaAlumno");
                int idGrupo = rs.getInt("idGrupo");
                Float nota = rs.getFloat("nota");
                if(rs.wasNull()){
                    nota = null;
                }
                matricula = new Matricula(idMatricula, cedulaAlumno, idGrupo, nota);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de Matrícula: " + e.getMessage());
        } finally {
            try {
                if(rs != null)
                    rs.close();
                if(pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch(SQLException e){
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return matricula;
    }

    // Modifica una matrícula
    public void modificarMatricula(Matricula matricula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch(ClassNotFoundException e){
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch(SQLException e){
            throw new NoDataException("La BD no se encuentra disponible: " + e.getMessage());
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarMatricula);
            pstmt.setInt(1, matricula.getIdMatricula());
            pstmt.setString(2, matricula.getCedulaAlumno());
            pstmt.setInt(3, matricula.getIdGrupo());
            if(matricula.getNota() != null){
                pstmt.setFloat(4, matricula.getNota());
            } else {
                pstmt.setNull(4, Types.FLOAT);
            }
            pstmt.registerOutParameter(5, Types.INTEGER);
            pstmt.execute();
            int rows = pstmt.getInt(5);
            if(rows == 0){
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación de Matrícula Satisfactoria!");
        } catch(SQLException e){
            e.printStackTrace();
            throw new GlobalException("Error en la modificación de Matrícula: " + e.getMessage());
        } finally {
            try {
                if(pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch(SQLException e){
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public void eliminarMatricula(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("Driver no localizado: " + e.getMessage());
        } catch (SQLException e) {
            throw new NoDataException("La BD no se encuentra disponible: " + e.getMessage());
        }

        CallableStatement pstmt = null;
        try {

            pstmt = this.conexion.prepareCall("{call eliminarMatricula(?, ?)}");
            pstmt.setInt(1, id);
            pstmt.registerOutParameter(2, Types.INTEGER);
            pstmt.execute();

            int resultado = pstmt.getInt(2);
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Matrícula no encontrada.");
            }

            System.out.println("\nEliminación de Matrícula Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la eliminación de Matrícula: " + e.getMessage());
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
