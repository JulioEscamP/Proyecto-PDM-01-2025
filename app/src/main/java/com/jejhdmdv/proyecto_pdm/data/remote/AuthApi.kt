package com.jejhdmdv.proyecto_pdm.data.remote

import com.jejhdmdv.proyecto_pdm.data.GoogleSignInRequest
import com.jejhdmdv.proyecto_pdm.data.LoginRequest
import com.jejhdmdv.proyecto_pdm.data.LoginResponse
import com.jejhdmdv.proyecto_pdm.data.RegisterRequest
import com.jejhdmdv.proyecto_pdm.data.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("auth/login") // TODO: La ruta en el backend para autentificar el login
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/google-login") //Google Login Endpoint
    suspend fun googleLogin(@Body request: GoogleSignInRequest): Response<LoginResponse>

}