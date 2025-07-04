package com.jejhdmdv.proyecto_pdm.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.admin.ClinicRepository
import com.jejhdmdv.proyecto_pdm.model.admin.Clinic
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmergencyViewModel(private val clinicRepository: ClinicRepository) : ViewModel() {

    private val _clinics = MutableStateFlow<Resource<List<Clinic>>>(Resource.Loading())
    val clinics: StateFlow<Resource<List<Clinic>>> = _clinics.asStateFlow()

    init {
        fetchClinics()
    }

    private fun fetchClinics() {
        viewModelScope.launch {
            clinicRepository.getActiveClinics().collect {
                _clinics.value = it
            }
        }
    }
}


