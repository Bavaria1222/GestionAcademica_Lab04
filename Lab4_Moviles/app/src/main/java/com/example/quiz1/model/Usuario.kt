// app/src/main/java/com/example/quiz1/model/Usuario.kt
package com.example.quiz1.model

import java.io.Serializable

data class Usuario(
    val cedula: String,
    val clave: String,
    val rol: String
) : Serializable
