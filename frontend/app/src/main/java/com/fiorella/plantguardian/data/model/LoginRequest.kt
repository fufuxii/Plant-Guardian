package com.fiorella.plantguardian.data.model

data class LoginRequest(
    val correo: String,
    val password: String
)

data class LoginResponse(
    val mensaje: String,
    val usuario: Any? // Puedes poner Any? o crear una clase para los datos del usuario
)