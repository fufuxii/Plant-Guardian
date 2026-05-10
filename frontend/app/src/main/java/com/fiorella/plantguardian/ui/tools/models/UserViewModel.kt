package com.fiorella.plantguardian.ui.tools.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiorella.plantguardian.data.network.RetrofitClient
import com.fiorella.plantguardian.data.schemas.AchievementData
import com.fiorella.plantguardian.data.schemas.UserProgressData
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _usuario = MutableLiveData<UserProgressData>()
    private val _cargando = MutableLiveData<Boolean>()
    private val _logros = MutableLiveData<List<AchievementData>>()
    val usuario: LiveData<UserProgressData> = _usuario
    val cargando: LiveData<Boolean> = _cargando
    val logros: LiveData<List<AchievementData>> = _logros

    fun cargarDatosUsuario(idUsuario: String) {
        _cargando.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerUsuarioInfo(idUsuario)
                if (response.isSuccessful) {
                    _usuario.value = response.body()
                }
            } catch (e: Exception) {
            } finally {
                _cargando.value = false
            }
        }
    }

    fun cargarLogrosUsuario(idUsuario: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerLogrosUsuario(idUsuario)
                if (response.isSuccessful) {
                    val lista = response.body()
                    Log.d("DEBUG_LOGROS", "Logros recibidos: ${lista?.size ?: 0}")
                    Log.d("DEBUG_LOGROS", "Contenido: $lista") // Verifica si el JSON mapea bien
                    _logros.value = lista ?: emptyList()
                } else {
                    Log.e("DEBUG_LOGROS", "Error API: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_LOGROS", "Fallo total: ${e.message}")
            }
        }
    }
}