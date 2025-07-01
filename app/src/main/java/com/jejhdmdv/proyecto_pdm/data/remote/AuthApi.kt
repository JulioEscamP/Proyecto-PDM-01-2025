package com.jejhdmdv.proyecto_pdm.data.remote

import com.jejhdmdv.proyecto_pdm.model.login.GoogleSignInRequest
import com.jejhdmdv.proyecto_pdm.model.login.LoginRequest
import com.jejhdmdv.proyecto_pdm.model.login.LoginResponse
import com.jejhdmdv.proyecto_pdm.model.login.RegisterRequest
import com.jejhdmdv.proyecto_pdm.model.login.RegisterResponse
import com.jejhdmdv.proyecto_pdm.model.login.VetRegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/google-login")
    suspend fun googleLogin(@Body request: GoogleSignInRequest): Response<LoginResponse>

    // --- NUEVO ENDPOINT PARA REGISTRO DE VETERINARIO ---
    @POST("auth/vet-register") // TODO: Asegúrate de que esta sea la ruta correcta en tu backend
    suspend fun vetRegister(@Body request: VetRegisterRequest): Response<RegisterResponse>
}