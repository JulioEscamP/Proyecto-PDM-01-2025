package com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetloginviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository

class VetLoginViewModelFactory(authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VetLoginViewModel::class.java)) {
            return VetLoginViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

