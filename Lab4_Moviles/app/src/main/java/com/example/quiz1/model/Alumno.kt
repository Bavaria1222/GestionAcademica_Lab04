package com.example.gestionacademicaapp.model

import java.io.Serializable

data class Alumno(
    val idAlumno: Int,
    val nombre: String,
    val telefono: String,
    val email: String,
    val fechaNacimiento: String,  // en formato yyyy-MM-dd
    val idCarrera: Int,
    val cedula: String
): Serializable
