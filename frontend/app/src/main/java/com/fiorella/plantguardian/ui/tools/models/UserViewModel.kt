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
        if (_usuario.value != null) return

        _cargando.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerUsuarioInfo(idUsuario)
                if (response.isSuccessful) {
                    _usuario.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_USER", "Error: ${e.message}")
            } finally {
                _cargando.value = false
            }
        }
    }

    fun cargarLogrosUsuario(idUsuario: String) {
        if (_logros.value != null) return

        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.obtenerLogrosUsuario(idUsuario)
                if (response.isSuccessful) {
                    _logros.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                Log.e("DEBUG_LOGROS", "Fallo total: ${e.message}")
            }
        }
    }

    fun refrescarDatos(idUsuario: String) {
        _usuario.value = null
        _logros.value = null
        cargarDatosUsuario(idUsuario)
        cargarLogrosUsuario(idUsuario)
    }
}