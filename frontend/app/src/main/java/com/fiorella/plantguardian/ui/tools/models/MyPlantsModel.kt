package com.fiorella.plantguardian.ui.tools.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

class MyPlantsModel : ViewModel() {

    private val _plantas = MutableLiveData<List<PlantData>>()
    private val _estaCargando = MutableLiveData<Boolean>()
    val plantas: LiveData<List<PlantData>> = _plantas
    private var yaCargado = false

    fun obtenerPlantas(idUsuario: String) {
        if (yaCargado) return

        _estaCargando.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerPlantasUsuario(idUsuario)
                if (response.isSuccessful) {
                    _plantas.value = response.body() ?: emptyList()
                    yaCargado = true
                }
            } catch (e: Exception) {
            } finally {
                _estaCargando.value = false
            }
        }
    }
}