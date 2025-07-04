package com.jejhdmdv.proyecto_pdm.model.admin

//estado de una cita
enum class AppointmentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    COMPLETED,
    CANCELLED
}

//modelo de datos
data class Appointment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userEmail: String = "",
    val petName: String = "",
    val petType: String = "",
    val appointmentDate: String = "",
    val appointmentTime: String = "",
    val reason: String = "",
    val status: AppointmentStatus = AppointmentStatus.PENDING,
    val adminNotes: String = "",
    val createdAt: String = "",
    val updatedAt: String = ""
)

//modelo para crear una cita
data class AppointmentRequest(
    val userId: String,
    val petName: String,
    val petType: String,
    val appointmentDate: String,
    val appointmentTime: String,
    val reason: String
)

//actaliza el estadod de la cita
data class AppointmentStatusUpdate(
    val appointmentId: String,
    val status: AppointmentStatus,
    val adminNotes: String = ""
)

//respuesta del servidor
data class AppointmentResponse(
    val success: Boolean,
    val message: String,
    val appointment: Appointment? = null,
    val appointments: List<Appointment>? = null
)

