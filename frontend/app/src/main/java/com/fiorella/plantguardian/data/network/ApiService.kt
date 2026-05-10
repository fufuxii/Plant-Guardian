package com.fiorella.plantguardian.data.network

import com.fiorella.plantguardian.data.schemas.AchievementData
import com.fiorella.plantguardian.data.schemas.AnalisisResponse
import com.fiorella.plantguardian.data.schemas.GenericResponse
import com.fiorella.plantguardian.data.schemas.WeatherData
import com.fiorella.plantguardian.data.schemas.LoginRequest
import com.fiorella.plantguardian.data.schemas.LoginResponse
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.data.schemas.PlantResponse
import com.fiorella.plantguardian.data.schemas.RegisterRequest
import com.fiorella.plantguardian.data.schemas.TaskData
import com.fiorella.plantguardian.data.schemas.UserProgressData
import com.fiorella.plantguardian.data.schemas.UserRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("clima/usuario/{user_id}")
    suspend fun obtenerClima(@Path("user_id") userId: String): Response<WeatherData>

    @POST("usuarios/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("usuarios/auth/registro")
    suspend fun registro(@Body request: RegisterRequest): Response<Any>

    @GET("usuarios/{id}/info")
    suspend fun obtenerUsuarioInfo(
        @Path("id") idUsuario: String
    ): Response<UserProgressData>

    @GET("usuarios/{id}/logros")
    suspend fun obtenerLogrosUsuario(
        @Path("id") idUsuario: String
    ): Response<List<AchievementData>>

    @Multipart
    @POST("plantas/identificar")
    fun identificarPlanta(@Part imagen: MultipartBody.Part): Call<PlantResponse>

    @POST("plantas/analizar/{temp_id}")
    suspend fun analizarPlanta(
        @Path("temp_id") tempId: String,
        @Query("lugar") lugar: String,
        @Query("id_usuario") idUsuario: String
    ): Response<AnalisisResponse>

    @POST("plantas/guardar/{temp_id}")
    suspend fun guardarPlanta(
        @Path("temp_id") tempId: String,
        @Body usuario: UserRequest
    ): Response<GenericResponse>

    @GET("plantas/obtener/{id_usuario}")
    suspend fun obtenerPlantasUsuario(
        @Path("id_usuario") idUsuario: String
    ): Response<List<PlantData>>

    @GET("tareas/planta/{id_usuario_planta}")
    suspend fun obtenerTareasPlanta(
        @Path("id_usuario_planta") idUsuarioPlanta: String
    ): Response<List<TaskData>>
}