package com.fiorella.plantguardian.data.network

import com.fiorella.plantguardian.data.model.ClimaData
import com.fiorella.plantguardian.data.model.LoginRequest
import com.fiorella.plantguardian.data.model.LoginResponse
import com.fiorella.plantguardian.data.model.RegistroRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("usuarios/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuarios/auth/registro")
    suspend fun registro(@Body request: RegistroRequest): Response<Any>

    @GET("/clima/usuario/{user_id}")
    suspend fun obtener_clima(@Path("user_id") userId: String): Response<ClimaData>
}