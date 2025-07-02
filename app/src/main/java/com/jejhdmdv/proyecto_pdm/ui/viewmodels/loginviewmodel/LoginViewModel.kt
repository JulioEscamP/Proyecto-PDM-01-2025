package com.jejhdmdv.proyecto_pdm.ui.viewmodels.loginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository
import com.jejhdmdv.proyecto_pdm.model.login.LoginRequest
import com.jejhdmdv.proyecto_pdm.model.login.LoginResponse
<<<<<<< Updated upstream
=======
import com.jejhdmdv.proyecto_pdm.model.login.RegisterRequest
import com.jejhdmdv.proyecto_pdm.model.login.RegisterResponse
import com. jejhdmdv.proyecto_pdm.model.login.VetRegisterRequest
>>>>>>> Stashed changes
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableStateFlow<Resource<LoginResponse>>(Resource.Loading())
    val loginResult: StateFlow<Resource<LoginResponse>> = _loginResult.asStateFlow()

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

}