package com.example.appsaborperu.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit para conectarse a la API REST.
 */
object RetrofitClient {
    
    // Para emulador Android: 10.0.2.2 apunta al localhost de la máquina host
    // Para dispositivo físico: usar la IP de tu computadora (ej: 192.168.1.100)
    private const val BASE_URL = "http://10.0.2.2:8080/api/"
    
    // Interceptor para logs (útil para debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // Cliente OkHttp con configuración
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Instancia de Retrofit
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    // API de productos
    val productApi: ProductApi = retrofit.create(ProductApi::class.java)
}

