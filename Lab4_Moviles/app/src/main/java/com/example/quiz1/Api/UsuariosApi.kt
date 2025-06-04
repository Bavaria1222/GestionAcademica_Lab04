
package com.example.quiz1.api

import com.example.quiz1.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UsuarioApi {
    @GET("usuarios")           // GET http://BASE_URL/usuario
    fun listar(): Call<List<Usuario>>

    @GET("usuarios/{cedula}")  // GET http://BASE_URL/usuario/S12345
    fun buscar(@Path("cedula") cedula: String): Call<Usuario>

    @POST("usuarios")          // POST http://BASE_URL/usuario
    fun insertar(@Body usuario: Usuario): Call<Void>

    @PUT("usuarios")           // PUT http://BASE_URL/usuario
    fun modificar(@Body usuario: Usuario): Call<Void>

    @DELETE("usuarios/{cedula}") // DELETE http://BASE_URL/usuario/S12345
    fun eliminar(@Path("cedula") cedula: String): Call<Void>
}
