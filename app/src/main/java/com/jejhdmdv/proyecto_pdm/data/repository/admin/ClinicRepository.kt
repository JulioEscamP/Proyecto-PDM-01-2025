package com.jejhdmdv.proyecto_pdm.data.repository.admin

import com.jejhdmdv.proyecto_pdm.data.remote.admin.ClinicApi
import com.jejhdmdv.proyecto_pdm.model.admin.Clinic
import com.jejhdmdv.proyecto_pdm.model.admin.ClinicRequest
import com.jejhdmdv.proyecto_pdm.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

/**
 * Repositorio para la gestión de clínicas veterinarias
 * Maneja la comunicación entre el ViewModel y la API
 */
class ClinicRepository(private val clinicApi: ClinicApi) {
    
   //obtener todas las citas
    fun getAllClinics(): Flow<Resource<List<Clinic>>> = flow {
        try {
            emit(Resource.Loading())
            val response = clinicApi.getAllClinics()
            if (response.isSuccessful) {
                response.body()?.let { clinicResponse ->
                    if (clinicResponse.success) {
                        emit(Resource.Success(clinicResponse.clinics ?: emptyList()))
                    } else {
                        emit(Resource.Error(clinicResponse.message))
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
    
   //clinicas activas
    fun getActiveClinics(): Flow<Resource<List<Clinic>>> = flow {
        try {
            emit(Resource.Loading())
            val response = clinicApi.getActiveClinics()
            if (response.isSuccessful) {
                response.body()?.let { clinicResponse ->
                    if (clinicResponse.success) {
                        emit(Resource.Success(clinicResponse.clinics ?: emptyList()))
                    } else {
                        emit(Resource.Error(clinicResponse.message))
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
    
    //clinica por ID
    fun getClinicById(clinicId: String): Flow<Resource<Clinic>> = flow {
        try {
            emit(Resource.Loading())
            val response = clinicApi.getClinicById(clinicId)
            if (response.isSuccessful) {
                response.body()?.let { clinicResponse ->
                    if (clinicResponse.success && clinicResponse.clinic != null) {
                        emit(Resource.Success(clinicResponse.clinic))
                    } else {
                        emit(Resource.Error(clinicResponse.message))
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
    
  //crear nueva clinica
    fun createClinic(clinicRequest: ClinicRequest): Flow<Resource<Clinic>> = flow {
        try {
            emit(Resource.Loading())
            val response = clinicApi.createClinic(clinicRequest)
            if (response.isSuccessful) {
                response.body()?.let { clinicResponse ->
                    if (clinicResponse.success && clinicResponse.clinic != null) {
                        emit(Resource.Success(clinicResponse.clinic))
                    } else {
                        emit(Resource.Error(clinicResponse.message))
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
    
   //actializar una clinica
    fun updateClinic(clinicId: String, clinicRequest: ClinicRequest): Flow<Resource<Clinic>> = flow {
        try {
            emit(Resource.Loading())
            val response = clinicApi.updateClinic(clinicId, clinicRequest)
            if (response.isSuccessful) {
                response.body()?.let { clinicResponse ->
                    if (clinicResponse.success && clinicResponse.clinic != null) {
                        emit(Resource.Success(clinicResponse.clinic))
                    } else {
                        emit(Resource.Error(clinicResponse.message))
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
    
    //eliminar una clinica
    fun deleteClinic(clinicId: String): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val response = clinicApi.deleteClinic(clinicId)
            if (response.isSuccessful) {
                response.body()?.let { clinicResponse ->
                    if (clinicResponse.success) {
                        emit(Resource.Success(clinicResponse.message))
                    } else {
                        emit(Resource.Error(clinicResponse.message))
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

