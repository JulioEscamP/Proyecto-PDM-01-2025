package com.jejhdmdv.proyecto_pdm.ui.admin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jejhdmdv.proyecto_pdm.data.repository.admin.AppointmentRepository
import com.jejhdmdv.proyecto_pdm.model.admin.Appointment
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentStatus
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentStatusUpdate
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class AdminAppointmentViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {
    

    private val _appointmentsState = MutableStateFlow<Resource<List<Appointment>>>(Resource.Loading())
    val appointmentsState: StateFlow<Resource<List<Appointment>>> = _appointmentsState.asStateFlow()
    

    private val _operationState = MutableStateFlow<Resource<String>?>(null)
    val operationState: StateFlow<Resource<String>?> = _operationState.asStateFlow()
    
    init {
        loadAppointments()
    }
    

    fun loadAppointments() {
        viewModelScope.launch {
            appointmentRepository.getAllAppointments().collect { result ->
                _appointmentsState.value = result
            }
        }
    }
    

    fun loadPendingAppointments() {
        viewModelScope.launch {
            appointmentRepository.getPendingAppointments().collect { result ->
                _appointmentsState.value = result
            }
        }
    }
    

    fun approveAppointment(appointmentId: String, adminNotes: String = "") {
        viewModelScope.launch {
            val statusUpdate = AppointmentStatusUpdate(
                appointmentId = appointmentId,
                status = AppointmentStatus.APPROVED,
                adminNotes = adminNotes
            )
            
            appointmentRepository.updateAppointmentStatus(appointmentId, statusUpdate).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Cita aprobada exitosamente")
                        loadAppointments()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al aprobar cita")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                }
            }
        }
    }
    

    fun rejectAppointment(appointmentId: String, adminNotes: String = "") {
        viewModelScope.launch {
            val statusUpdate = AppointmentStatusUpdate(
                appointmentId = appointmentId,
                status = AppointmentStatus.REJECTED,
                adminNotes = adminNotes
            )
            
            appointmentRepository.updateAppointmentStatus(appointmentId, statusUpdate).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Cita rechazada exitosamente")
                        loadAppointments()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al rechazar cita")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                }
            }
        }
    }
    
   //actualiza el estado de la citas
    fun updateAppointmentStatus(appointmentId: String, status: AppointmentStatus, adminNotes: String = "") {
        viewModelScope.launch {
            val statusUpdate = AppointmentStatusUpdate(
                appointmentId = appointmentId,
                status = status,
                adminNotes = adminNotes
            )
            
            appointmentRepository.updateAppointmentStatus(appointmentId, statusUpdate).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _operationState.value = Resource.Success("Estado de cita actualizado exitosamente")
                        loadAppointments()
                    }
                    is Resource.Error -> {
                        _operationState.value = Resource.Error(result.message ?: "Error al actualizar estado de cita")
                    }
                    is Resource.Loading -> {
                        _operationState.value = Resource.Loading()
                    }
                }
            }
        }
    }
    
    //para limpiar el estado de la operacion
    fun clearOperationState() {
        _operationState.value = null
    }
}


class AdminAppointmentViewModelFactory(
    private val appointmentRepository: AppointmentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminAppointmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminAppointmentViewModel(appointmentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

