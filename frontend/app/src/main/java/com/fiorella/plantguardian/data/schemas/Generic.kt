package com.fiorella.plantguardian.data.schemas

data class GenericResponse(
    val mensaje: String,
    val data: Any? = null
)