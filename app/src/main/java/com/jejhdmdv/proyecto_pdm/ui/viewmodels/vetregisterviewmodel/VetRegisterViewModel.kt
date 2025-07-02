package com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetregisterviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository
import com.jejhdmdv.proyecto_pdm.model.login.RegisterResponse
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.jejhdmdv.proyecto_pdm.model.login.VetRegisterRequest

class VetRegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _registerResult = MutableStateFlow<Resource<RegisterResponse>?>(null)
    val registerResult: StateFlow<Resource<RegisterResponse>?> = _registerResult

    fun registerVet(request: VetRegisterRequest) {
        viewModelScope.launch {
            _registerResult.value = Resource.Loading()
            val result = authRepository.vetRegister(request)
            _registerResult.value = result
        }
    }
}
