package com.jejhdmdv.proyecto_pdm.ui.admin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.admin.ClinicRepository
import com.jejhdmdv.proyecto_pdm.model.admin.Clinic
import com.jejhdmdv.proyecto_pdm.model.admin.ClinicRequest
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class AdminClinicViewModel(
    private val clinicRepository: ClinicRepository
) : ViewModel() {
    
    // Estado de la lista de clínicas
    private val _clinicsState = MutableStateFlow<Resource<List<Clinic>>>(Resource.Loading())
    val clinicsState: StateFlow<Resource<List<Clinic>>> = _clinicsState.asStateFlow()
    
    // Estado de la clínica individual (para edición)
    private val _clinicState = MutableStateFlow<Resource<Clinic>?>(null)
    val clinicState: StateFlow<Resource<Clinic>?> = _clinicState.asStateFlow()
    
    // Estado de operaciones (crear, actualizar, eliminar)
    private val _operationState = MutableStateFlow<Resource<String>?>(null)
    val operationState: StateFlow<Resource<String>?> = _operationState.asStateFlow()
    
    init {
        loadClinics()
    }
    
    //cargar todas las clinicas
    fun loadClinics() {
        viewModelScope.launch {
            clinicRepository.getAllClinics().collect { result ->
                _clinicsState.value = result
            }
        }
    }
    
    //clinicas por ID
    fun loadClinic(clinicId: String) {
        viewModelScope.launch {
            clinicRepository.getClinicById(clinicId).collect { result ->
                _clinicState.value = result
            }
        }
    }
    
    //crear una nueva clinica
    fun createClinic(clinicRequest: ClinicRequest) {
        viewModelScope.launch {
            clinicRepository.createClinic(clinicRequest).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Clínica creada exitosamente")
                        loadClinics()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al crear clínica")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                }
            }
        }
    }
    

    fun updateClinic(clinicId: String, clinicRequest: ClinicRequest) {
        viewModelScope.launch {
            clinicRepository.updateClinic(clinicId, clinicRequest).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Clínica actualizada exitosamente")
                        loadClinics()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al actualizar clínica")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                }
            }
        }
    }
    

    fun deleteClinic(clinicId: String) {
        viewModelScope.launch {
            clinicRepository.deleteClinic(clinicId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Clínica eliminada exitosamente")
                        loadClinics()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al eliminar clínica")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                }
            }
        }
    }
    

    fun clearOperationState() {
        _operationState.value = null
    }
    

    fun clearClinicState() {
        _clinicState.value = null
    }
}


class AdminClinicViewModelFactory(
    private val clinicRepository: ClinicRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminClinicViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminClinicViewModel(clinicRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

