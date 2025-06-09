package com.jejhdmdv.proyecto_pdm.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.LoginRequest
import com.jejhdmdv.proyecto_pdm.data.LoginResponse
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> = _loginResult

    fun performLogin(email: String, password: String) {
        _loginResult.value = Resource.Loading()
        viewModelScope.launch {
            val request = LoginRequest(email, password)
            _loginResult.value = authRepository.login(request)
        }
    }

    fun signInWithGoogle(idToken: String) {
        _loginResult.value = Resource.Loading()
        viewModelScope.launch {
            _loginResult.value = authRepository.googleLogin(idToken)
        }
    }

}