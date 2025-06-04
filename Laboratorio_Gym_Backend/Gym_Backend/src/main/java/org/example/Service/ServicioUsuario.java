package org.example.Service;

import org.example.Entity.Usuario;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class ServicioUsuario extends Servicio {

    // Definición de las llamadas a los procedimientos/funciones almacenadas en Oracle
    private static final String insertarUsuario = "{call insertarUsuario(?,?,?)}";
    private static final String listarUsuario = "{? = call listarUsuario()}";
    private static final String buscarUsuario = "{? = call buscarUsuario(?)}";
    private static final String modificarUsuario = "{call modificarUsuario(?,?,?,?)}";
    private static final String eliminarUsuario = "{call eliminarUsuario(?)}";

    public ServicioUsuario() {
    }


    public void insertarUsuario(Usuario usuario) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(insertarUsuario);
            pstmt.setString(1, usuario.getCedula());
            pstmt.setString(2, usuario.getClave());
            pstmt.setString(3, usuario.getRol());

            // Ejecutar la llamada al procedimiento
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


    public Collection<Usuario> listarUsuario() throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el Driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        ArrayList<Usuario> coleccion = new ArrayList<>();
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(listarUsuario);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("cedula"),
                        rs.getString("clave"),
                        rs.getString("rol")
                );
                coleccion.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
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
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }

        if (!coleccion.isEmpty()) {
            return coleccion;
        } else {
            throw new NoDataException("No hay datos");
        }
    }


    public Usuario buscarUsuario(String cedula) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }

        ResultSet rs = null;
        Usuario usuario = null;
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(buscarUsuario);
            pstmt.registerOutParameter(1, -10); // -10 equivale a OracleTypes.CURSOR
            pstmt.setString(2, cedula);
            pstmt.execute();
            rs = (ResultSet) pstmt.getObject(1);

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getString("cedula"),
                        rs.getString("clave"),
                        rs.getString("rol")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
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
                throw new GlobalException("Estatutos inválidos o nulos");
            }
        }
        return usuario;
    }


    public void modificarUsuario(Usuario usuario) throws GlobalException, NoDataException {
        try {
            this.conectar();
        } catch (ClassNotFoundException e) {
            throw new GlobalException("No se ha localizado el driver");
        } catch (SQLException e) {
            throw new NoDataException("La base de datos no se encuentra disponible");
        }
        CallableStatement pstmt = null;
        try {
            pstmt = this.conexion.prepareCall(modificarUsuario);
            pstmt.setString(1, usuario.getCedula());
            pstmt.setString(2, usuario.getClave());
            pstmt.setString(3, usuario.getRol());
            // Registrar el parámetro de salida que devuelve el número de filas afectadas
            pstmt.registerOutParameter(4, java.sql.Types.INTEGER);

            pstmt.execute();
            int rows = pstmt.getInt(4);
            if (rows == 0) {
                throw new NoDataException("No se realizó la actualización");
            }
            System.out.println("\nModificación Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Sentencia no válida");
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


    public void eliminarUsuario(String cedula) throws GlobalException, NoDataException {
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
            String eliminarUsuario = "{call eliminarUsuario(?, ?)}";
            pstmt = this.conexion.prepareCall(eliminarUsuario);
            pstmt.setString(1, cedula);
            pstmt.registerOutParameter(2, java.sql.Types.INTEGER); // filas afectadas

            pstmt.execute();
            int resultado = pstmt.getInt(2);

            if (resultado == 0) {
                throw new NoDataException("No se realizó el borrado. Usuario no encontrado.");
            }

            System.out.println("\nEliminación de Usuario Satisfactoria!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new GlobalException("Error en la eliminación de Usuario: " + e.getMessage());
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
