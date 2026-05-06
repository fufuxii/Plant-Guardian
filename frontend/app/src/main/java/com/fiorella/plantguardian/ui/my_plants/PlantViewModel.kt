package com.fiorella.plantguardian.ui.my_plants
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiorella.plantguardian.data.model.PlantData
import com.fiorella.plantguardian.data.network.RetrofitClient
import kotlinx.coroutines.launch

class PlantViewModel : ViewModel() {

    private val _plantas = MutableLiveData<List<PlantData>>()
    private val _estaCargando = MutableLiveData<Boolean>()
    val plantas: LiveData<List<PlantData>> = _plantas
    val estaCargando: LiveData<Boolean> = _estaCargando
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
                // Si falla, dejamos yaCargado en false para reintentar luego
            } finally {
                _estaCargando.value = false
            }
        }
    }
}