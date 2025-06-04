package org.example.Controller;

import org.example.Entity.Usuario;
import org.example.Exeption.GlobalException;
import org.example.Exeption.NoDataException;
import org.example.Service.ServicioUsuario;

import java.util.Collection;

public class ControlUsuario {

    private ServicioUsuario servicioUsuario;

    public ControlUsuario() {
        this.servicioUsuario = new ServicioUsuario();
    }

    // Inserta un nuevo usuario
    public void insertarUsuario(Usuario usuario) throws GlobalException, NoDataException {
        servicioUsuario.insertarUsuario(usuario);
    }

    // Lista todos los usuarios
    public Collection<Usuario> listarUsuario() throws GlobalException, NoDataException {
        return servicioUsuario.listarUsuario();
    }

    // Busca un usuario por cédula
    public Usuario buscarUsuario(String cedula) throws GlobalException, NoDataException {
        return servicioUsuario.buscarUsuario(cedula);
    }

    // Modifica un usuario
    public void modificarUsuario(Usuario usuario) throws GlobalException, NoDataException {
        servicioUsuario.modificarUsuario(usuario);
    }

    // Elimina un usuario por cédula
    public void eliminarUsuario(String cedula) throws GlobalException, NoDataException {
        servicioUsuario.eliminarUsuario(cedula);
    }
}
