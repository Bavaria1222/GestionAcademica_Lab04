package com.example.quiz1.model

import java.io.Serializable

data class Profesor(
    val cedula: String,
    val nombre: String,
    val telefono: String,
    val email: String
) : Serializable
