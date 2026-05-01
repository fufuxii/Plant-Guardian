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