package com.fiorella.plantguardian.data.model

data class PlantResponse(
    val temp_id: String?,
    val resultado: PlantData?
)

data class PlantData(
    val nombre_cientifico: String?,
    val nombre_comun: String?,
    val error: String?
)

data class AnalisisResponse(
    val estado: String,
    val problema: String,
    val descripcion: String,
    val consejos: List<String>,
    val tareas: List<TareaResponse>
)

data class TareaResponse(
    val tarea: String,
    val frecuencia: String,
    val experiencia: Int
)