package com.jejhdmdv.proyecto_pdm.data.repository

import com.jejhdmdv.proyecto_pdm.data.LoginRequest
import com.jejhdmdv.proyecto_pdm.data.LoginResponse
import com.jejhdmdv.proyecto_pdm.data.remote.AuthApiService
import com.jejhdmdv.proyecto_pdm.utils.Resource
import com.jejhdmdv.proyecto_pdm.data.GoogleSignInRequest
import com.jejhdmdv.proyecto_pdm.data.RegisterRequest
import com.jejhdmdv.proyecto_pdm.data.RegisterResponse


class AuthRepository(private val authApiService: AuthApiService) {

    suspend fun login(request: LoginRequest): Resource<LoginResponse> {
        return try {
            val response = authApiService.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("Respuesta de login vacía")
                }
            } else {
                // Parsear el error body (en caso API devuelve errores en JSON)
                val errorBody = response.errorBody()?.string()
                Resource.Error("Error de login: ${response.code()} - ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error("Error de red al intentar login: ${e.localizedMessage}")
        }
    }

    suspend fun googleLogin(idToken: String): Resource<LoginResponse> {
        return try {
            val request = GoogleSignInRequest(idToken)
            val response = authApiService.googleLogin(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {

                    Resource.Error<LoginResponse>("Respuesta de Google Login vacía")
                }
            } else {
                val errorBody = response.errorBody()?.string()

                Resource.Error<LoginResponse>("Error de Google Login: ${response.code()} - ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {

            Resource.Error<LoginResponse>("Error de red al intentar Google Login: ${e.localizedMessage}")
        }
    }

    suspend fun register(request: RegisterRequest): Resource<RegisterResponse> {
        return try {
            val response = authApiService.register(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error<RegisterResponse>("Respuesta de registro vacía")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Resource.Error<RegisterResponse>("Error de registro: ${response.code()} - ${errorBody ?: response.message()}")
            }
        } catch (e: Exception) {
            Resource.Error<RegisterResponse>("Error de red al intentar registrar: ${e.localizedMessage}")
        }
    }
}