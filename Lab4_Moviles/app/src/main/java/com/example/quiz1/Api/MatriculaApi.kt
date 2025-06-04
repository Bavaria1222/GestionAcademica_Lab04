package com.example.quiz1.api

import com.example.quiz1.model.Matricula
import retrofit2.Call
import retrofit2.http.*

interface MatriculaApi {

    @GET("matriculas")
    fun listar(): Call<List<Matricula>>

    @POST("matriculas")
    fun insertar(@Body matricula: Matricula): Call<Void>

    @PUT("matriculas")
    fun modificar(@Body matricula: Matricula): Call<Void>

    @DELETE("matriculas/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>

    @GET("matriculas/alumno/{cedula}")
    fun listarPorAlumno(@Path("cedula") cedula: String): Call<List<Matricula>>

    @GET("matriculas/grupo/{idGrupo}")
    fun listarPorGrupo(@Path("idGrupo") idGrupo: Int): Call<List<Matricula>>
}
