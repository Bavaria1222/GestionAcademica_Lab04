package com.example.quiz1.api

import com.example.quiz1.model.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("login")
    fun login(@Body usuario: Usuario): Call<Usuario>
}
