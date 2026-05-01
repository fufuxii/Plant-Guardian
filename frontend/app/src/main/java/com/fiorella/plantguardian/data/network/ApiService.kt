package com.fiorella.plantguardian.data.network

import com.fiorella.plantguardian.data.model.WeatherData
import com.fiorella.plantguardian.data.model.LoginRequest
import com.fiorella.plantguardian.data.model.LoginResponse
import com.fiorella.plantguardian.data.model.PlantResponse
import com.fiorella.plantguardian.data.model.RegisterRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("usuarios/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuarios/auth/registro")
    suspend fun registro(@Body request: RegisterRequest): Response<Any>

    @GET("clima/usuario/{user_id}")
    suspend fun obtener_clima(@Path("user_id") userId: String): Response<WeatherData>

    @Multipart
    @POST("plantas/identificar")
    fun identificarPlanta(@Part imagen: MultipartBody.Part): Call<PlantResponse>
}