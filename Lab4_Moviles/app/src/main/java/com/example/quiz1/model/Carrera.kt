
package com.example.gestionacademicaapp.model
import java.io.Serializable

data class Carrera(
    val idCarrera: Int,
    val codigo: String,
    val nombre: String,
    val titulo: String
) : Serializable
