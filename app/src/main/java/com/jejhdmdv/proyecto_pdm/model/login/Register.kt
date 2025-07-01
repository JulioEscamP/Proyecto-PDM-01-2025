package com.jejhdmdv.proyecto_pdm.model.login

data class RegisterRequest(
    val nombre: String,
    val edad: String,
    val dui: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val password: String,
    val passwordConfirmation: String
)

data class RegisterResponse(
    val message: String,
    val userId: String?
)

data class VetRegisterRequest(
    val nombre: String,
    val email: String,
    val telefono: String,
    val direccion: String,
    val numero_de_registro: String, // Nuevo campo para veterinarios
    val password: String,
    val passwordConfirmation: String
)

data class GoogleSignInRequest(
    val idToken: String
)