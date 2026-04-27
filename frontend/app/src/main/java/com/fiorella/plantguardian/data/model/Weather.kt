package com.fiorella.plantguardian.data.model

data class WeatherData(
    val temp: Double,
    val humedad: Int,
    val viento: Double,
    val lluvia: Double,
    val descripcion: String,
    val icono: String
)