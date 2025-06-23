package com.jejhdmdv.proyecto_pdm.model.login

data class LoginRequest(
    val email: String,
    val password: String
)


data class LoginResponse(
    val userId: String,
    val accessToken: String, // Token del RetrofitClient
    val username: String?
)