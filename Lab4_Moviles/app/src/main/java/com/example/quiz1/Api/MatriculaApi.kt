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
}
