package com.example.quiz1.model

import java.io.Serializable

data class Matricula(
    val idMatricula: Int,
    val cedulaAlumno: String,
    val idGrupo: Int,
    val nota: Float?
): Serializable
