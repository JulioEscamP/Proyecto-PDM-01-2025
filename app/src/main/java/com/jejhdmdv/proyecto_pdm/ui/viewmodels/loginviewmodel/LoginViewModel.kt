package com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository
import com.jejhdmdv.proyecto_pdm.model.login.LoginRequest
import com.jejhdmdv.proyecto_pdm.model.login.LoginResponse
import com.jejhdmdv.proyecto_pdm.model.login.RegisterRequest
import com.jejhdmdv.proyecto_pdm.model.login.RegisterResponse
import com.jejhdmdv.proyecto_pdm.model.login.VetRegisterRequest
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // --- ESTADO PARA LOGIN ---
    private val _loginResult = MutableStateFlow<Resource<LoginResponse>>(Resource.Idle()) // Inicia en Idle
    val loginResult: StateFlow<Resource<LoginResponse>> = _loginResult.asStateFlow()

    // --- ESTADO PARA REGISTRO ---
    private val _registerResult = MutableStateFlow<Resource<RegisterResponse>>(Resource.Idle())
    val registerResult: StateFlow<Resource<RegisterResponse>> = _registerResult.asStateFlow()

    // --- ESTADO PARA REGISTRO VETERINARIO ---
    private val _vetRegisterResult = MutableStateFlow<Resource<RegisterResponse>>(Resource.Idle())
    val vetRegisterResult: StateFlow<Resource<RegisterResponse>> = _vetRegisterResult.asStateFlow()

    fun performLogin(email: String, password: String) {
        _loginResult.value = Resource.Loading()
        viewModelScope.launch {
            _loginResult.value = authRepository.login(LoginRequest(email, password))
        }
    }

    fun signInWithGoogle(idToken: String) {
        _loginResult.value = Resource.Loading()
        viewModelScope.launch {
            _loginResult.value = authRepository.googleLogin(idToken)
        }
    }

    // --- Función para Registro de Usuario ---
    fun performRegister(request: RegisterRequest) {
        _registerResult.value = Resource.Loading()
        viewModelScope.launch {
            _registerResult.value = authRepository.register(request)
        }
    }

    // --- NUEVA FUNCIÓN PARA REGISTRO DE VETERINARIO ---
    fun performVetRegister(request: VetRegisterRequest) {
        _vetRegisterResult.value = Resource.Loading()
        viewModelScope.launch {
            _vetRegisterResult.value = authRepository.vetRegister(request)
        }
    }

    // Función para resetear el estado (útil al navegar fuera de la pantalla)
    fun resetRegisterState() {
        _registerResult.value = Resource.Idle()
    }

    fun resetVetRegisterState() {
        _vetRegisterResult.value = Resource.Idle()
    }
}

