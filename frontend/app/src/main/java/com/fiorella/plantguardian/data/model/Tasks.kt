package com.fiorella.plantguardian.data.model
import java.io.Serializable

data class TaskData(
    val id: String,
    val titulo: String,
    val hecho: Boolean,
    val fecha_proxima: String,
    val frecuencia_numerica: Int,
    val frecuencia_textual: String?
) : Serializable

data class TaskResponse(
    val tarea: String,
    val frecuencia: String,
    val experiencia: Int
) : Serializable