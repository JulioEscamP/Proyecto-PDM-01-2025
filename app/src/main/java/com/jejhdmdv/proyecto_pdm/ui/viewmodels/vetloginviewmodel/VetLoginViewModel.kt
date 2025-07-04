package com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetloginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VetLoginViewModel : ViewModel() {

    private val _loginResult = MutableStateFlow<Resource<Boolean>>(Resource.Idle()) //
    val loginResult: StateFlow<Resource<Boolean>> = _loginResult


    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            if (email == "vet@example.com" && password == "1234") {
                _loginResult.value = Resource.Success(true)
            } else {
                _loginResult.value = Resource.Error("Credenciales inválidas")
            }
        }
    }
    fun resetLoginResult() {
        _loginResult.value = Resource.Loading() // Reset
    }
}
