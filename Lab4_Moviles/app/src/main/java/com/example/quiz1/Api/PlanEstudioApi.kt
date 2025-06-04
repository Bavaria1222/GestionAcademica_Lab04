package com.example.quiz1.api

import com.example.quiz1.model.PlanEstudio
import retrofit2.Call
import retrofit2.http.*

interface PlanEstudioApi {

    @GET("planEstudios")
    fun listar(): Call<List<PlanEstudio>>

    @POST("planEstudios")
    fun insertar(@Body plan: PlanEstudio): Call<Void>

    @PUT("planEstudios")
    fun actualizar(@Body plan: PlanEstudio): Call<Void>

    @DELETE("planEstudios/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>
}
