package com.example.quiz1.api

import com.example.quiz1.model.Curso
import retrofit2.Call
import retrofit2.http.*

interface CursoApi {
    @GET("cursos")
    fun listar(): Call<List<Curso>>

    @GET("cursos/{id}")
    fun buscar(@Path("id") id: Int): Call<Curso>

    @POST("cursos")
    fun insertar(@Body curso: Curso): Call<Void>

    @PUT("cursos")
    fun modificar(@Body curso: Curso): Call<Void>

    @DELETE("cursos/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>
}
