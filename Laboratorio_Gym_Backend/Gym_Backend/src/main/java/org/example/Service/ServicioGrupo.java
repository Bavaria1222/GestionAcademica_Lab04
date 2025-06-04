package org.example.Service;

import org.example.Entity.Grupo;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

public class


ServicioGrupo extends Servicio {

    // Cadenas para llamar a los procedimientos y funciones de Grupo en Oracle
    private static final String insertarGrupo = "{call insertarGrupo(?,?,?,?,?)}";
    private static final String listarGrupo = "{? = call listarGrupo()}";
    private static final String buscarGrupo = "{? = call buscarGrupo(?)}";
    private static final String modificarGrupo = "{call modificarGrupo(?,?,?,?,?, ?,?)}";
    private static final String eliminarGrupo = "{call eliminarGrupo(?)}";
    private static final String listarGrupoPorProfesor =
            "{? = call listarGrupoPorProfesor(?, ?)}";

    public ServicioGrupo() {
    }


    public void insertarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarGrupo);
            pstmt.setInt(1, grupo.getIdCiclo());
            pstmt.setInt(2, grupo.getIdCurso());
            pstmt.setInt(3, grupo.getNumGrupo());
            pstmt.setString(4, grupo.getHorario());
            pstmt.setString(5, grupo.getIdProfesor());
            pstmt.execute();
            System.out.println("\nInserción de Grupo Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la inserción de Grupo: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public Collection<Grupo> listarGrupo() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }
        ArrayList<Grupo> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarGrupo);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Grupo grupo = new Grupo(
                        rs.getInt("idGrupo"),
                        rs.getInt("idCiclo"),
                        rs.getInt("idCurso"),
                        rs.getInt("numGrupo"),
                        rs.getString("horario"),
                        rs.getString("idProfesor")
                );
                lista.add(grupo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar Grupos: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
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

    // Lista los grupos de un profesor en un ciclo específico
    public Collection<Grupo> listarGrupoProfesor(int idCiclo, String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }

        ArrayList<Grupo> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarGrupoPorProfesor);
            pstmt.registerOutParameter(1, -10);
            pstmt.setInt(2, idCiclo);
            pstmt.setString(3, cedula);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Grupo grupo = new Grupo(
                        rs.getInt("idGrupo"),
                        rs.getInt("idCiclo"),
                        rs.getInt("idCurso"),
                        rs.getInt("numGrupo"),
                        rs.getString("horario"),
                        rs.getString("idProfesor")
                );
                lista.add(grupo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar Grupos: " + e.getMessage());
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


    public Grupo buscarGrupo(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }
        Grupo grupo = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarGrupo);
            pstmt.registerOutParameter(1, -10); // OracleTypes.CURSOR
            pstmt.setInt(2, id);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                grupo = new Grupo(
                        rs.getInt("idGrupo"),
                        rs.getInt("idCiclo"),
                        rs.getInt("idCurso"),
                        rs.getInt("numGrupo"),
                        rs.getString("horario"),
                        rs.getString("idProfesor")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de Grupo: " + e.getMessage());
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return grupo;
    }


    public void modificarGrupo(Grupo grupo) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarGrupo);
            pstmt.setInt(1, grupo.getIdGrupo());
            pstmt.setInt(2, grupo.getIdCiclo());
            pstmt.setInt(3, grupo.getIdCurso());
            pstmt.setInt(4, grupo.getNumGrupo());
            pstmt.setString(5, grupo.getHorario());
            pstmt.setString(6, grupo.getIdProfesor());
            pstmt.registerOutParameter(7, Types.INTEGER);
            pstmt.execute();
            int rows = pstmt.getInt(7);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación de Grupo Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la modificación de Grupo: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public void eliminarGrupo(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException | SQLException e) {
            throw new GlobalException("Error en la conexión: " + e.getMessage());
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall("{call eliminarGrupo(?, ?)}");
            pstmt.setInt(1, id);
            pstmt.registerOutParameter(2, Types.INTEGER); // OUT: filas afectadas

            pstmt.execute();
            int resultado = pstmt.getInt(2); // valor del parámetro OUT

            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Grupo no encontrado.");
            }

            System.out.println("\nEliminación de Grupo Satisfactoria!");
        } catch (SQLException e) {
            throw new GlobalException("Error en la eliminación de Grupo: " + e.getMessage());
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
