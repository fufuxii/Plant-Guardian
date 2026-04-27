package com.fiorella.plantguardian.data.model

data class RegisterRequest(
    val nombre: String,
    val correo: String,
    val password: String,
    val ubicacion: String
)