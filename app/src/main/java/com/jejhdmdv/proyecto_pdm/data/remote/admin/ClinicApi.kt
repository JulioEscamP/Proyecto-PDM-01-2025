package com.jejhdmdv.proyecto_pdm.data.remote.admin

import com.jejhdmdv.proyecto_pdm.model.admin.Clinic
import com.jejhdmdv.proyecto_pdm.model.admin.ClinicRequest
import com.jejhdmdv.proyecto_pdm.model.admin.ClinicResponse
import retrofit2.Response
import retrofit2.http.*


 //API para la gestión de clínicas veterinarias por parte del administrador

interface ClinicApi {
    

    @GET("admin/clinics")
    suspend fun getAllClinics(): Response<ClinicResponse>
    

    @GET("admin/clinics/{id}")
    suspend fun getClinicById(@Path("id") clinicId: String): Response<ClinicResponse>
    

    @POST("admin/clinics")
    suspend fun createClinic(@Body clinicRequest: ClinicRequest): Response<ClinicResponse>
    

    @PUT("admin/clinics/{id}")
    suspend fun updateClinic(
        @Path("id") clinicId: String,
        @Body clinicRequest: ClinicRequest
    ): Response<ClinicResponse>
    

    @DELETE("admin/clinics/{id}")
    suspend fun deleteClinic(@Path("id") clinicId: String): Response<ClinicResponse>
    

    @GET("clinics")
    suspend fun getActiveClinics(): Response<ClinicResponse>
}

