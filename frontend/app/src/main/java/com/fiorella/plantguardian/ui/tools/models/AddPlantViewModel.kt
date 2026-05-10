package com.fiorella.plantguardian.ui.tools.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fiorella.plantguardian.data.schemas.AnalisisResponse

class AddPlantViewModel : ViewModel() {
    val resultadoAnalisis = MutableLiveData<AnalisisResponse?>()
    var ultimoLugarAnalizado: String? = null
    var ultimoTempIdAnalizado: String? = null
}