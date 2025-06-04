package com.example.quiz1.api
import com.example.quiz1.model.Grupo
import retrofit2.Call
import retrofit2.http.*


interface GrupoApi {
    @GET("grupos")
    fun listar(): Call<List<Grupo>>

    @POST("grupos")
    fun insertar(@Body g: Grupo): Call<Void>

    @PUT("grupos")
    fun modificar(@Body g: Grupo): Call<Void>

    @DELETE("grupos/{id}")
    fun eliminar(@Path("id") id: Int): Call<Void>

    @GET("grupos/profesor/{cedula}/ciclo/{idCiclo}")
    fun listarPorProfesor(
        @Path("cedula") cedula: String,
        @Path("idCiclo") idCiclo: Int
    ): Call<List<Grupo>>
}

