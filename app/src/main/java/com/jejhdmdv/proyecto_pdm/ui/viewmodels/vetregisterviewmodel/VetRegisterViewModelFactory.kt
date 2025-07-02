package com.jejhdmdv.proyecto_pdm.ui.viewmodels.vetregisterviewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jejhdmdv.proyecto_pdm.data.repository.AuthRepository

class VetRegisterViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VetRegisterViewModel::class.java)) {
            return VetRegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
