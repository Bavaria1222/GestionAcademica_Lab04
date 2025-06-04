package org.example.Entity;

import java.time.LocalDate;

public class Alumno extends Usuario {
    private String nombre;
    private String telefono;
    private String email;
    private LocalDate fechaNacimiento;
    private int idCarrera;


    public Alumno() {
        super();
    }

    public Alumno(String cedula, String nombre, String telefono, String email, LocalDate fechaNacimiento, int idCarrera) {
        // el rol se establece como "ALUMNO" y la clave se maneja por separado.
        super(cedula, "", "ALUMNO");
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.idCarrera = idCarrera;
    }


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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "cedula='" + getCedula() + '\'' +
                ", clave='" + getClave() + '\'' +
                ", rol='" + getRol() + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", idCarrera=" + idCarrera +
                '}';
    }
}
