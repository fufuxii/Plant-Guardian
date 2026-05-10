package com.fiorella.plantguardian.data.schemas

import com.google.gson.annotations.SerializedName

data class UserData(
    val id: String,
    val nombre: String,
    val ubicacion: String
)

data class UserProgressData(
    val id: String,
    val nombre: String,
    val correo: String,
    val icono: String,
    val ubicacion: String,
    val nivel: Int,
    val experiencia_actual: Int,
    val experiencia_nivel: Int,
    val tareas_completadas: Int,
    val progreso_porcentaje: Int
)

data class UserRequest(
    val id_usuario: String
)

data class RegisterRequest(
    val nombre: String,
    val correo: String,
    val password: String,
    val ubicacion: String
)

data class LoginRequest(
    val correo: String,
    val password: String
)

data class LoginResponse(
    val mensaje: String,
    val usuario: UserData?
)

data class AchievementData(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val icono: String,
    val fecha_obtencion: String?
)

data class IconData(
    @SerializedName("url")
    val url: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("nombre")
    val nombre: String
)