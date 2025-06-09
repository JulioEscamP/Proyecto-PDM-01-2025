package com.jejhdmdv.proyecto_pdm.data

//data clases para el Login/Register

data class LoginRequest(
    val email: String,
    val password: String
)

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

data class LoginResponse(
    val userId: String,
    val accessToken: String, // Token del RetrofitClient
    val username: String?
)

data class ErrorResponse(
    val message: String,
    val code: Int?
)

data class GoogleSignInRequest(
    val idToken: String
)

//FIN de las data clases para login