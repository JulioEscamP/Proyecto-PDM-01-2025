package com.jejhdmdv.proyecto_pdm.data.remote.admin

import com.jejhdmdv.proyecto_pdm.model.admin.Appointment
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentRequest
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentResponse
import com.jejhdmdv.proyecto_pdm.model.admin.AppointmentStatusUpdate
import retrofit2.Response
import retrofit2.http.*

//gestion de citas
interface AppointmentApi {
    
   //obtener todas las citas para el adminsitrador
    @GET("admin/appointments")
    suspend fun getAllAppointments(): Response<AppointmentResponse>
    
    //citas pendietes
    @GET("admin/appointments/pending")
    suspend fun getPendingAppointments(): Response<AppointmentResponse>
    

    @GET("admin/appointments/{id}")
    suspend fun getAppointmentById(@Path("id") appointmentId: String): Response<AppointmentResponse>
    
    //crear una nueva cita -usuario
    @POST("appointments")
    suspend fun createAppointment(@Body appointmentRequest: AppointmentRequest): Response<AppointmentResponse>
    
    //actulaixa el estado de l acita
    @PUT("admin/appointments/{id}/status")
    suspend fun updateAppointmentStatus(
        @Path("id") appointmentId: String,
        @Body statusUpdate: AppointmentStatusUpdate
    ): Response<AppointmentResponse>
    

    @GET("appointments/user/{userId}")
    suspend fun getUserAppointments(@Path("userId") userId: String): Response<AppointmentResponse>
    
   //calcelar cita
    @PUT("appointments/{id}/cancel")
    suspend fun cancelAppointment(@Path("id") appointmentId: String): Response<AppointmentResponse>
}

