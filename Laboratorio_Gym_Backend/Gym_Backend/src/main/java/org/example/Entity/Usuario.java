package org.example.Entity;

public class Usuario {
    private String cedula;
    private String clave;
    private String rol; // Ejemplo: "ADMINISTRADOR", "MATRICULADOR", "PROFESOR", "ALUMNO"


    public Usuario() {
    }


    public Usuario(String cedula, String clave, String rol) {
        this.cedula = cedula;
        this.clave = clave;
        this.rol = rol;
    }


    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "cedula='" + cedula + '\'' +
                ", clave='" + clave + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
