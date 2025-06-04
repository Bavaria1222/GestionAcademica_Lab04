package com.example.quiz1.model

import java.io.Serializable


data class PlanEstudio(
    val idPlanEstudio: Int,
    val idCarrera: Int,
    val idCurso: Int,
    val anio: Int,
    val ciclo: Int
) : Serializable