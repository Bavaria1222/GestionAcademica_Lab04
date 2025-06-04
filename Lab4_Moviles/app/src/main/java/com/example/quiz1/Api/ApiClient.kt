package com.example.quiz1.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Puerto correcto del backend en la máquina anfitriona
    private const val BASE_URL = "http://10.0.2.2:8080/api/" // Usa localhost del emulador

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
