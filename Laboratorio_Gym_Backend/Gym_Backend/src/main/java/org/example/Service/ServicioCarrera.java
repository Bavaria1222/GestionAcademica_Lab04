package org.example.Service;

import org.example.Entity.Carrera;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioCarrera extends Servicio {

    private static final String insertarCarrera = "{call insertarcarrera(?,?,?,?)}";
    private static final String listarCarrera = "{? = call listarcarrera()}";
    private static final String buscarCarrera = "{? = call buscarCarrera(?)}";
    private static final String modificarCarrera = "{call modificarCarrera(?,?,?,?,?)}";
    private static final String eliminarCarrera = "{call eliminarCarrera(?, ?)}";

    public ServicioCarrera() {}

    public void insertarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarCarrera);
            pstmt.setInt(1, carrera.getIdCarrera());
            pstmt.setString(2, carrera.getCodigo());
            pstmt.setString(3, carrera.getNombre());
            pstmt.setString(4, carrera.getTitulo());

            pstmt.execute();
            System.out.println("\nInserción de Carrera satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la inserción de Carrera: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }

    public Collection<Carrera> listarCarrera() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ArrayList<Carrera> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarCarrera);
            pstmt.registerOutParameter(1, -10); // OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Carrera carrera = new Carrera(
                        rs.getInt("idcarrera"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("titulo")
                );
                lista.add(carrera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar carreras: " + e.getMessage());
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

    public Carrera buscarCarrera(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        Carrera carrera = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarCarrera);
            pstmt.registerOutParameter(1, -10);
            pstmt.setInt(2, id);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                carrera = new Carrera(
                        rs.getInt("idcarrera"),
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("titulo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de Carrera: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return carrera;
    }

    public void modificarCarrera(Carrera carrera) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarCarrera);
            pstmt.setInt(1, carrera.getIdCarrera());
            pstmt.setString(2, carrera.getCodigo());
            pstmt.setString(3, carrera.getNombre());
            pstmt.setString(4, carrera.getTitulo());
            pstmt.registerOutParameter(5, Types.INTEGER);

            pstmt.execute();
            int rows = pstmt.getInt(5);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la modificación de Carrera: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }
    public void eliminarCarrera(int id) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;

        try {
            pstmt = this.conexion.prepareCall("{call eliminarCarrera(?, ?)}");
            pstmt.setInt(1, id);                        // IN: ID de la carrera
            pstmt.registerOutParameter(2, Types.INTEGER); // OUT: número de filas afectadas

            pstmt.execute();

            int resultado = pstmt.getInt(2); // Recoge el número de filas afectadas
            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Carrera no encontrada.");
            }

            System.out.println("\nEliminación satisfactoria de la carrera con ID: " + id);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la eliminación de Carrera: " + e.getMessage());
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
