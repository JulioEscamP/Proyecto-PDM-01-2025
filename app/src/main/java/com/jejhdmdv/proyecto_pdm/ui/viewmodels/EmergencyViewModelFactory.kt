package com.jejhdmdv.proyecto_pdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jejhdmdv.proyecto_pdm.data.repository.admin.ClinicRepository

class EmergencyViewModelFactory(private val clinicRepository: ClinicRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmergencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmergencyViewModel(clinicRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


