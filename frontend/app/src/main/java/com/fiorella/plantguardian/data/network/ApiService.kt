package com.fiorella.plantguardian.data.network

import com.fiorella.plantguardian.data.model.LoginRequest
import com.fiorella.plantguardian.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("usuarios/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}