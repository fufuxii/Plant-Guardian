package com.fiorella.plantguardian.data.model

data class TemperaturaResponse(
    val clima_id: Int,
    val temp: Double,
    val humedad: Int,
    val viento: Double,
    val lluvia: Double,
    val descripcion: String,
    val icono: String
)