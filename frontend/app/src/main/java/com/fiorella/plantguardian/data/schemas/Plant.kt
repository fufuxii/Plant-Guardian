package com.fiorella.plantguardian.data.schemas
import java.io.Serializable

data class PlantResponse(
    val temp_id: String?,
    val resultado: PlantNameData?
)

data class PlantNameData(
    val nombre_cientifico: String?,
    val nombre_comun: String?,
    val error: String?
)

data class PlantData(
    val id_usuario_planta: String,
    val id_planta: String,
    val nombre_comun: String,
    val nombre_cientifico: String,
    val nombre_otros: String?,
    val lugar: String,
    val estado: String?,
    val imagen_url: String,
    val tareas: List<TaskData>?,
    val consejos: List<String>?,
    val problema: String?,
    val descripcion_usuario: String?,
    val descripcion_general: String?
) : Serializable

data class AnalisisResponse(
    val estado: String,
    val problema: String,
    val descripcion: String,
    val consejos: List<String>,
    val tareas: List<TaskResponse>
)

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