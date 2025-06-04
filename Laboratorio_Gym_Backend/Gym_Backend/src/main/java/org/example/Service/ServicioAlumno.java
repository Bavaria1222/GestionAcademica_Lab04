package org.example.Service;
import org.example.Entity.Alumno;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioAlumno extends Servicio {

    // Cadenas para llamar a los procedimientos y funciones en Oracle
    private static final String insertarAlumno = "{call insertarAlumnoCompleto(?,?,?,?,?,?,?)}";
    private static final String listarAlumno = "{? = call listarAlumno()}";
    private static final String buscarAlumno = "{? = call buscarAlumno(?)}";
    private static final String modificarAlumno = "{call modificarAlumno(?,?,?,?,?,?,?,?)}";
    private static final String eliminarAlumno = "{call eliminarAlumno(?, ?)}";


    public ServicioAlumno() {
    }


    public void insertarAlumno(Alumno alumno, String clave) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarAlumno);
            pstmt.setString(1, alumno.getCedula());
            pstmt.setString(2, clave);
            // Se asume que el rol para alumno es 'ALUMNO'
            pstmt.setString(3, alumno.getNombre()); // Aquí se envía el nombre para la tabla alumno
            pstmt.setString(4, alumno.getTelefono());
            pstmt.setString(5, alumno.getEmail());
            // Conversión de LocalDate a java.sql.Date
            pstmt.setDate(6, java.sql.Date.valueOf(alumno.getFechaNacimiento()));
            pstmt.setInt(7, alumno.getIdCarrera());

            pstmt.execute();
            System.out.println("\nInserción de Alumno Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la inserción de Alumno: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public Collection<Alumno> listarAlumno() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        ArrayList<Alumno> lista = new ArrayList<>();
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarAlumno);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            while (rs.next()) {
                Alumno alumno = new Alumno(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getInt("idCarrera")
                );
                lista.add(alumno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error al listar alumnos: " + e.getMessage());
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


    public Alumno buscarAlumno(String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        Alumno alumno = null;
        ResultSet rs = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarAlumno);
            pstmt.registerOutParameter(1, -10);
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);
            if (rs.next()) {
                alumno = new Alumno(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDate("fechaNacimiento").toLocalDate(),
                        rs.getInt("idCarrera")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la búsqueda de Alumno: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
        return alumno;
    }


    public void modificarAlumno(Alumno alumno, String clave) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarAlumno);
            pstmt.setString(1, alumno.getCedula());
            pstmt.setString(2, clave);
            pstmt.setString(3, alumno.getNombre());
            pstmt.setString(4, alumno.getTelefono());
            pstmt.setString(5, alumno.getEmail());
            pstmt.setDate(6, java.sql.Date.valueOf(alumno.getFechaNacimiento()));
            pstmt.setInt(7, alumno.getIdCarrera());
            pstmt.registerOutParameter(8, Types.INTEGER);
            pstmt.execute();
            int rows = pstmt.getInt(8);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la modificación de Alumno: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                this.desconectar();
            } catch (SQLException e) {
                throw new GlobalException("Error cerrando recursos: " + e.getMessage());
            }
        }
    }


    public void eliminarAlumno(String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            // Nuevo procedimiento con parámetro OUT
            String eliminarAlumno = "{call eliminarAlumno(?, ?)}";
            pstmt = this.conexion.prepareCall(eliminarAlumno);

            pstmt.setString(1, cedula);                      // IN: cédula del alumno
            pstmt.registerOutParameter(2, Types.INTEGER);    // OUT: resultado (número de filas afectadas)

            pstmt.execute();
            int resultado = pstmt.getInt(2);                 // Obtenemos el valor del parámetro OUT

            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Alumno no encontrado.");
            }

            System.out.println("\nEliminación Satisfactoria!");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la eliminación de Alumno: " + e.getMessage());
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
