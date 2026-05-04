package com.fiorella.plantguardian.data.model

data class UserData(
    val id: String,
    val nombre: String,
    val ubicacion: String
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