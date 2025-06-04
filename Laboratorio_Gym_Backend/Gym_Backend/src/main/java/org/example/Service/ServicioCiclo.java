package org.example.Service;

import org.example.Entity.Ciclo;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioCiclo extends Servicio {

    // Cadenas para invocar procedimientos y funciones en Oracle
    private static final String insertarCiclo = "{call insertarCiclo(?,?,?,?)}";
    private static final String lsitarCiclo = "{? = call listarCiclo()}";
    private static final String buscarCiclo = "{? = call buscarCiclo(?)}";
    private static final String modificarCiclo = "{call modificarCiclo(?,?,?,?,?,?)}";
    private static final String eliminarCiclo = "{call eliminarCiclo(?)}";

    public ServicioCiclo() {
    }


    public void insertarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarCiclo);
            pstmt.setInt(1, ciclo.getAnio());
            pstmt.setInt(2, ciclo.getNumero());
            pstmt.setDate(3, java.sql.Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(4, java.sql.Date.valueOf(ciclo.getFechaFin()));
            pstmt.execute();
            System.out.println("\nInserción de Ciclo Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la inserción de Ciclo: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public Collection<Ciclo> listarCiclo() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        ArrayList<Ciclo> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(lsitarCiclo);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Ciclo ciclo = new Ciclo(
                        rs.getInt("idCiclo"),
                        rs.getInt("anio"),
                        rs.getInt("numero"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin").toLocalDate()
                );
                lista.add(ciclo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar Ciclo: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
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


    public Ciclo buscarCiclo(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        Ciclo ciclo = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarCiclo);
            pstmt.registerOutParameter(1, -10);
            pstmt.setInt(2, id);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                ciclo = new Ciclo(
                        rs.getInt("idCiclo"),
                        rs.getInt("anio"),
                        rs.getInt("numero"),
                        rs.getDate("fechaInicio").toLocalDate(),
                        rs.getDate("fechaFin").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de Ciclo: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return ciclo;
    }


    public void modificarCiclo(Ciclo ciclo) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarCiclo);
            pstmt.setInt(1, ciclo.getIdCiclo());
            pstmt.setInt(2, ciclo.getAnio());
            pstmt.setInt(3, ciclo.getNumero());
            pstmt.setDate(4, java.sql.Date.valueOf(ciclo.getFechaInicio()));
            pstmt.setDate(5, java.sql.Date.valueOf(ciclo.getFechaFin()));
            pstmt.registerOutParameter(6, Types.INTEGER);
            pstmt.execute();
            int rows = pstmt.getInt(6);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación de Ciclo Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la modificación de Ciclo: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public void eliminarCiclo(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            String call = "{call eliminarCiclo(?, ?)}";
            pstmt = this.conexion.prepareCall(call);
            pstmt.setInt(1, id);                      // IN: id ciclo
            pstmt.registerOutParameter(2, Types.INTEGER); // OUT: filas afectadas

            pstmt.execute();
            int resultado = pstmt.getInt(2);
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Ciclo no encontrado.");
            }

            System.out.println("\nEliminación de Ciclo Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la eliminación de Ciclo: " + e.getMessage());
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