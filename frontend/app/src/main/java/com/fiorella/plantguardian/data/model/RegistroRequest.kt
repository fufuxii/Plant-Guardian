package com.fiorella.plantguardian.data.model

data class RegistroRequest(
    val nombre: String,
    val correo: String,
    val password: String,
    val ubicacion: String
)