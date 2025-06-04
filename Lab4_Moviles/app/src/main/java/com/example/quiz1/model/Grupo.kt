
package com.example.quiz1.model

import java.io.Serializable

data class Grupo(
    val idGrupo: Int,
    val idCiclo: Int,
    val idCurso: Int,
    val numGrupo: Int,
    val horario: String,
    val idProfesor: String
) : Serializable
