package com.example.quiz1.api

import com.example.gestionacademicaapp.model.Carrera
import retrofit2.Call
import retrofit2.http.*

interface CarreraApi {
    @GET("carreras")
    fun listar(): Call<List<Carrera>>

    @GET("carreras/{id}")
    fun buscar(@Path("id") id: Int): Call<Carrera>

    @POST("carreras")
    fun insertar(@Body carrera: Carrera): Call<Void>

    @PUT("carreras")
    fun modificar(@Body carrera: Carrera): Call<Void>

    @DELETE("carreras/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>
}
