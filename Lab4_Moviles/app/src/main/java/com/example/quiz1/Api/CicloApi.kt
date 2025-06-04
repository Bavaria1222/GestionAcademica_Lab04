package com.example.quiz1.api

import com.example.gestionacademicaapp.model.Ciclo
import retrofit2.Call
import retrofit2.http.*

interface CicloApi {
    @GET("ciclos")
    fun listar(): Call<List<Ciclo>>

    @GET("ciclos/{id}")
    fun buscar(@Path("id") id: Int): Call<Ciclo>

    @POST("ciclos")
    fun insertar(@Body ciclo: Ciclo): Call<Void>

    @PUT("ciclos")
    fun modificar(@Body ciclo: Ciclo): Call<Void>

    @DELETE("ciclos/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>
}
