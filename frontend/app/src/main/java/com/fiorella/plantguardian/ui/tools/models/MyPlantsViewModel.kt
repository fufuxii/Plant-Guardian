package com.fiorella.plantguardian.ui.tools.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiorella.plantguardian.data.schemas.PlantData
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

class MyPlantsViewModel : ViewModel() {

    private val _plantas = MutableLiveData<List<PlantData>?>()
    private val _estaCargando = MutableLiveData<Boolean>()
    val plantas: LiveData<List<PlantData>?> = _plantas
    val estaCargando: LiveData<Boolean> = _estaCargando

    private var yaCargado = false

    fun obtenerPlantas(idUsuario: String, forzarRecarga: Boolean = false) {
        if (yaCargado && !forzarRecarga) return
        _estaCargando.value = true
        if (forzarRecarga) _plantas.value = null

        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerPlantasUsuario(idUsuario)
                if (response.isSuccessful) {
                    _plantas.value = response.body() ?: emptyList()
                    yaCargado = true
                }
            } catch (e: Exception) {
                _plantas.value = emptyList()
            } finally {
                _estaCargando.value = false
            }
        }
    }

    fun eliminarPlanta(idUsuarioPlanta: String, idUsuario: String, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.eliminarPlanta(idUsuarioPlanta)
                if (response.isSuccessful) {
                    obtenerPlantas(idUsuario, forzarRecarga = true)
                    onResultado(true)
                } else {
                    onResultado(false)
                }
            } catch (e: Exception) {
                onResultado(false)
            }
        }
    }
}