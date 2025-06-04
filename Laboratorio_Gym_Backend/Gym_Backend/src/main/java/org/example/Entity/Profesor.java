package org.example.Entity;

public class Profesor extends Usuario {
    private String nombre;
    private String telefono;
    private String email;

    // Constructor por defecto
    public Profesor() {
        super();
    }


    public Profesor(String cedula, String nombre, String telefono, String email) {
        // clave y rol por defecto (clave vacía y rol "PROFESOR")
        super(cedula, "", "PROFESOR");
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "cedula='" + getCedula() + '\'' +
                ", clave='" + getClave() + '\'' +
                ", rol='" + getRol() + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
