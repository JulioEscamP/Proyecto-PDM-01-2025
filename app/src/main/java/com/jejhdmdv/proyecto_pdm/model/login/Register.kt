package com.jejhdmdv.proyecto_pdm.model.login

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
    val passwordConfirmation: String
)

data class RegisterResponse(
    val userId: String,
    val accessToken: String, // Token del RetrofitClient
    val username: String?
)

data class GoogleSignInRequest(
    val idToken: String
)