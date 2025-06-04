package org.example.Service;

import org.example.Entity.Curso;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioCurso extends Servicio {

    // Cadena para llamar a los procedimientos/funciones almacenadas
    private static final String insertarCurso = "{call insertarCurso(?,?,?,?)}";
    private static final String listarCurso = "{? = call listarCurso()}";
    private static final String buscarCurso = "{? = call buscarCurso(?)}";
    private static final String modificarCurso = "{call modificarCurso(?,?,?,?,?,?)}";
    private static final String eliminarCurso = "{call eliminarCurso(?, ?)}";

    public ServicioCurso() {
    }


    public void insertarCurso(Curso curso) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarCurso);
            pstmt.setString(1, curso.getCodigo());
            pstmt.setString(2, curso.getNombre());
            pstmt.setInt(3, curso.getCreditos());
            pstmt.setInt(4, curso.getHorasSemanales());

            boolean resultado = pstmt.execute();
            if (resultado) {
                throw new NoDataException("No se realizó la inserción");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Llave duplicada u otro error SQL");
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
    }


    public Collection<Curso> listarCurso() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        ResultSet rs = null;
        ArrayList<Curso> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarCurso);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Curso curso = new Curso(
                        rs.getInt("idcurso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("creditos"),
                        rs.getInt("horasSemanales")
                );
                coleccion.add(curso);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
        if (!coleccion.isEmpty()) {
            return coleccion;
        } else {
            throw new NoDataException("No hay datos");
        }
    }


    public Curso buscarCurso(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        ResultSet rs = null;
        Curso curso = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarCurso);
            pstmt.registerOutParameter(1, -10); // OracleTypes.CURSOR
            pstmt.setInt(2, id);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                curso = new Curso(
                        rs.getInt("idcurso"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("creditos"),
                        rs.getInt("horasSemanales")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
        return curso;
    }


    public void modificarCurso(Curso curso) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarCurso);
            pstmt.setInt(1, curso.getIdCurso());
            pstmt.setString(2, curso.getCodigo());
            pstmt.setString(3, curso.getNombre());
            pstmt.setInt(4, curso.getCreditos());
            pstmt.setInt(5, curso.getHorasSemanales());
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
            throw new GlobalException("Sentencia no válida");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
    }


    public void eliminarCurso(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(eliminarCurso);
            pstmt.setInt(1, id);
            pstmt.registerOutParameter(2, java.sql.Types.INTEGER);
            pstmt.execute();
            int resultado = pstmt.getInt(2);
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Curso no encontrado.");
            }
            System.out.println("\nEliminación Satisfactoria!");
        } catch (SQLException e) {
            throw new GlobalException("Error en la eliminación de Curso: " + e.getMessage());
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
