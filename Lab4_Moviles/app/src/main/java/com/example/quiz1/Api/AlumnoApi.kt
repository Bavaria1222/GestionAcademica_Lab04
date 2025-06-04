package com.example.quiz1.api

import com.example.gestionacademicaapp.model.Alumno
import retrofit2.Call
import retrofit2.http.*

interface AlumnoApi {

    @GET("alumnos")
    fun listar(): Call<List<Alumno>>

    @GET("alumnos/{id}")
    fun buscar(@Path("id") id: Int): Call<Alumno>

    @POST("alumnos")
    fun insertar(@Body alumno: Alumno): Call<Void>

    @PUT("alumnos")
    fun modificar(@Body alumno: Alumno): Call<Void>

    @DELETE("alumnos/{id}")
    fun eliminar(@Path("id") id: String): Call<Void>
}
