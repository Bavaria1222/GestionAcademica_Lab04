package com.example.quiz1.model

import java.io.Serializable

data class Curso(
    val idCurso: Int,
    val codigo: String,
    val nombre: String,
    val creditos: Int,
    val horasSemanales: Int
) : Serializable
