package com.example.gestionacademicaapp.model

import java.io.Serializable

data class Ciclo(
    val idCiclo: Int = 0,
    val anio: Int,
    val numero: Int,
    val fechaInicio: String, // "yyyy-MM-dd"
    val fechaFin: String     // "yyyy-MM-dd"
): Serializable
