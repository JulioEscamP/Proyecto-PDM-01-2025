package com.jejhdmdv.proyecto_pdm.data.repository.admin

import com.jejhdmdv.proyecto_pdm.data.remote.admin.AppointmentApi
import com.jejhdmdv.proyecto_pdm.model.admin.Appointment
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentRequest
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentStatusUpdate
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repositorio para la gestión de citas médicas
 * Maneja la comunicación entre el ViewModel y la API
 */
class AppointmentRepository(private val appointmentApi: AppointmentApi) {
    
   //obtener todas las citas
    fun getAllAppointments(): Flow<Resource<List<Appointment>>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.getAllAppointments()
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success) {
                        emit(Resource.Success(appointmentResponse.appointments ?: emptyList()))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    //citas pendeintes de aprobacion
    fun getPendingAppointments(): Flow<Resource<List<Appointment>>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.getPendingAppointments()
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success) {
                        emit(Resource.Success(appointmentResponse.appointments ?: emptyList()))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    

    fun getAppointmentById(appointmentId: String): Flow<Resource<Appointment>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.getAppointmentById(appointmentId)
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success && appointmentResponse.appointment != null) {
                        emit(Resource.Success(appointmentResponse.appointment))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    //crear una nueva cita de usuario final
    fun createAppointment(appointmentRequest: AppointmentRequest): Flow<Resource<Appointment>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.createAppointment(appointmentRequest)
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success && appointmentResponse.appointment != null) {
                        emit(Resource.Success(appointmentResponse.appointment))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    /**
     * Actualizar el estado de una cita (administrador)
     */
    fun updateAppointmentStatus(appointmentId: String, statusUpdate: AppointmentStatusUpdate): Flow<Resource<Appointment>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.updateAppointmentStatus(appointmentId, statusUpdate)
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success && appointmentResponse.appointment != null) {
                        emit(Resource.Success(appointmentResponse.appointment))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    //obtener cita
    fun getUserAppointments(userId: String): Flow<Resource<List<Appointment>>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.getUserAppointments(userId)
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success) {
                        emit(Resource.Success(appointmentResponse.appointments ?: emptyList()))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
   //cancelar cita
    fun cancelAppointment(appointmentId: String): Flow<Resource<Appointment>> = flow {
        try {
            emit(Resource.Loading())
            val response = appointmentApi.cancelAppointment(appointmentId)
            if (response.isSuccessful) {
                response.body()?.let { appointmentResponse ->
                    if (appointmentResponse.success && appointmentResponse.appointment != null) {
                        emit(Resource.Success(appointmentResponse.appointment))
                    } else {
                        emit(Resource.Error(appointmentResponse.message))
                    }
                } ?: emit(Resource.Error("Respuesta vacía del servidor"))
            } else {
                emit(Resource.Error("Error del servidor: ${response.code()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión: ${e.localizedMessage}"))
        } catch (e: IOException) {
            emit(Resource.Error("Error de red: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
}

