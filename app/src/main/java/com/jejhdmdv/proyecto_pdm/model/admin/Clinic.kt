package com.jejhdmdv.proyecto_pdm.model.admin

/**
 * Modelo de datos para clínicas veterinarias de emergencia
 */
data class Clinic(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val schedule: String = "",
    val isActive: Boolean = true,
    val createdAt: String = "",
    val updatedAt: String = ""
)

//actualiza la clinica
data class ClinicRequest(
    val name: String,
    val phoneNumber: String,
    val address: String,
    val schedule: String,
    val isActive: Boolean = true
)

//respuesta del servidor
data class ClinicResponse(
    val success: Boolean,
    val message: String,
    val clinic: Clinic? = null,
    val clinics: List<Clinic>? = null
)

